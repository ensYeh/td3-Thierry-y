package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public final class Dns {

  private static final String CONFIG_FILE = "config.properties";

  private static final String DB_FILE_PROPERTY_KEY = "database.file";

  private final Map<NomMachine, DnsItem> itemsByName;

  private final Map<AdresseIP, DnsItem> itemsByIp;

  private final Path databasePath;

  public Dns() throws IOException {
    this.itemsByName = new HashMap<>();
    this.itemsByIp = new HashMap<>();
    this.databasePath = this.loadConfig();
    this.loadDatabase();
  }

  private Path loadConfig() throws IOException {
    Properties props = new Properties();

    try (InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (is == null) {
        throw new IOException("Fichier de propriétés '" + CONFIG_FILE 
            + "' non trouvé dans les ressources.");
      }
      props.load(is);
    }

    String dbFileName = props.getProperty(DB_FILE_PROPERTY_KEY);
    if (dbFileName == null || dbFileName.isBlank()) {
      throw new IOException("La clé '" + DB_FILE_PROPERTY_KEY 
          + "' est absente du fichier " + CONFIG_FILE);
    }

    return Path.of(dbFileName);
  }

  private void loadDatabase() throws IOException {
    if (!Files.exists(this.databasePath)) {
      return;
    }

    List<String> lines = Files.readAllLines(this.databasePath);
    for (String line : lines) {
      if (line.isBlank() || line.startsWith("#")) {
        continue; 
      }

      String[] parts = line.split("\\s+"); 
      if (parts.length != 2) {
        System.err.println("Ligne mal formée ignorée : " + line);
        continue;
      }

      try {
        NomMachine nom = new NomMachine(parts[0]);
        AdresseIP ip = new AdresseIP(parts[1]);
        DnsItem item = new DnsItem(nom, ip);

        this.itemsByName.put(nom, item);
        this.itemsByIp.put(ip, item);

      } catch (IllegalArgumentException e) {
        System.err.println("Donnée invalide ignorée : " + line 
            + " (" + e.getMessage() + ")");
      }
    }
  }

  private void saveDatabase() throws IOException {
    List<String> lines = this.itemsByName.values().stream()
        .map(DnsItem::formatForFile)
        .collect(Collectors.toList());

    Files.write(this.databasePath, lines);
  }

  public DnsItem getItem(final NomMachine nom) {
    Objects.requireNonNull(nom);
    return this.itemsByName.get(nom);
  }

  public DnsItem getItem(final AdresseIP ip) {
    Objects.requireNonNull(ip);
    return this.itemsByIp.get(ip);
  }

  public List<DnsItem> getItems(final String domaine) {
    Objects.requireNonNull(domaine);
    
    return this.itemsByName.values().stream()
        .filter(item -> item.getNom().belongsToDomaine(domaine))
        .collect(Collectors.toList());
  }

  public void addItem(final AdresseIP ip, final NomMachine nom) throws IOException {

    if (this.itemsByIp.containsKey(ip)) {
      throw new IllegalArgumentException("ERREUR : L'adresse IP existe déjà !");
    }
    if (this.itemsByName.containsKey(nom)) {
      throw new IllegalArgumentException("ERREUR : Le nom de machine existe déjà !");
    }

    DnsItem newItem = new DnsItem(nom, ip);
    this.itemsByName.put(nom, newItem);
    this.itemsByIp.put(ip, newItem);

    this.saveDatabase();
  }
}
