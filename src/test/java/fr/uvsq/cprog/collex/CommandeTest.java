package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommandeTest {

  private final Path dbPath = Path.of("test_db.txt");
  private Dns dns;
  private DnsTUI tui;
  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;

  private final NomMachine nomUvsq = new NomMachine("www.uvsq.fr");
  private final AdresseIP ipUvsq = new AdresseIP("193.51.31.90");

  private final NomMachine nomEcampus = new NomMachine("ecampus.uvsq.fr");
  private final AdresseIP ipEcampus = new AdresseIP("193.51.25.12");

  private final NomMachine nomPoste = new NomMachine("poste.uvsq.fr");
  private final AdresseIP ipPoste = new AdresseIP("193.51.31.154");

  private final NomMachine nomGoogle = new NomMachine("www.google.com");
  private final AdresseIP ipGoogle = new AdresseIP("8.8.8.8");

  @Before
  public void setUp() throws IOException {
    List<String> lines = Arrays.asList(
        nomUvsq.getValeur() + " " + ipUvsq.getValeur(),
        nomEcampus.getValeur() + " " + ipEcampus.getValeur(),
        nomPoste.getValeur() + " " + ipPoste.getValeur(),
        nomGoogle.getValeur() + " " + ipGoogle.getValeur()
    );
    Files.write(dbPath, lines);
    dns = new Dns();
    tui = new DnsTUI();
    
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outputStream, true));
  }

  @After
  public void tearDown() throws IOException {
    System.setOut(originalOut);
    tui.close();
    Files.deleteIfExists(dbPath);
  }

  private String getOutput() {
    return outputStream.toString().trim();
  }

  @Test
  public void testGetIpCommandSuccess() {
    Commande cmd = new GetIpCommand(nomUvsq);
    cmd.execute(dns, tui);
    assertEquals("193.51.31.90", getOutput());
  }

  @Test
  public void testGetIpCommandNotFound() {
    NomMachine unknown = new NomMachine("unknown.uvsq.fr");
    Commande cmd = new GetIpCommand(unknown);
    cmd.execute(dns, tui);
    assertEquals("ERREUR : Nom de machine inconnu.", getOutput());
  }

  @Test
  public void testGetNameCommandSuccess() {
    Commande cmd = new GetNameCommand(ipUvsq);
    cmd.execute(dns, tui);
    assertEquals("www.uvsq.fr", getOutput());
  }

  @Test
  public void testGetNameCommandNotFound() {
    AdresseIP unknown = new AdresseIP("1.2.3.4");
    Commande cmd = new GetNameCommand(unknown);
    cmd.execute(dns, tui);
    assertEquals("ERREUR : Adresse IP inconnue.", getOutput());
  }

  @Test
  public void testAddCommandSuccess() throws IOException {
    NomMachine newNom = new NomMachine("pikachu.uvsq.fr");
    AdresseIP newIp = new AdresseIP("193.51.25.24");

    Commande cmd = new AddCommand(newIp, newNom);
    cmd.execute(dns, tui);

    DnsItem item = dns.getItem(newNom);
    assertNotNull("L'item doit être ajouté", item);
    assertEquals(newIp, item.getIp());

    List<String> lines = Files.readAllLines(dbPath);
    assertEquals(5, lines.size());
    assertTrue(lines.contains("pikachu.uvsq.fr 193.51.25.24"));
  }

  @Test
  public void testAddCommandDuplicateName() {
    AdresseIP newIp = new AdresseIP("1.2.3.4");
    Commande cmd = new AddCommand(newIp, nomUvsq);
    cmd.execute(dns, tui);
    
    assertEquals("ERREUR : Le nom de machine existe déjà !", getOutput());
  }

  @Test
  public void testAddCommandDuplicateIp() {
    NomMachine newNom = new NomMachine("test.machine.fr");
    Commande cmd = new AddCommand(ipUvsq, newNom);
    cmd.execute(dns, tui);
    
    assertEquals("ERREUR : L'adresse IP existe déjà !", getOutput());
  }

  @Test
  public void testLsCommandSortedByName() {
    Commande cmd = new LsCommand("uvsq.fr", false);
    cmd.execute(dns, tui);

    String output = getOutput();
    String[] lines = output.split("\n");
    
    assertEquals(3, lines.length);
    assertTrue(lines[0].contains("ecampus.uvsq.fr"));
    assertTrue(lines[1].contains("poste.uvsq.fr"));
    assertTrue(lines[2].contains("www.uvsq.fr"));
  }

  @Test
  public void testLsCommandSortedByIp() {
    Commande cmd = new LsCommand("uvsq.fr", true);
    cmd.execute(dns, tui);

    String output = getOutput();
    String[] lines = output.split("\n");
    
    assertEquals(3, lines.length);
    assertTrue(lines[0].contains("193.51.25.12"));
    assertTrue(lines[1].contains("193.51.31.154"));
    assertTrue(lines[2].contains("193.51.31.90"));
  }

  @Test
  public void testLsCommandEmptyDomain() {
    Commande cmd = new LsCommand("unknown.com", false);
    cmd.execute(dns, tui);
    
    assertEquals("Aucune machine trouvée pour le domaine : unknown.com", getOutput());
  }

  @Test
  public void testLsCommandSingleDomain() {
    Commande cmd = new LsCommand("google.com", false);
    cmd.execute(dns, tui);

    String output = getOutput();
    assertTrue(output.contains("www.google.com"));
    assertTrue(output.contains("8.8.8.8"));
  }

  @Test
  public void testQuitCommand() {
    Commande cmd = new QuitCommand();
    cmd.execute(dns, tui);
  }

  @Test
  public void testInvalidCommand() {
    String errorMessage = "Commande invalide";
    Commande cmd = new InvalidCommand(errorMessage);
    cmd.execute(dns, tui);
    
    assertEquals(errorMessage, getOutput());
  }
}

