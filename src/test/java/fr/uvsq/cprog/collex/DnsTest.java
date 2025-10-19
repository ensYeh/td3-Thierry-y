package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DnsTest {

  private final Path dbPath = Path.of("test_db.txt");

  private final NomMachine nomUvsq = new NomMachine("www.uvsq.fr");
  private final AdresseIP ipUvsq = new AdresseIP("193.51.31.90");

  private final NomMachine nomEcampus = new NomMachine("ecampus.uvsq.fr");
  private final AdresseIP ipEcampus = new AdresseIP("193.51.25.12");

  private final NomMachine nomGoogle = new NomMachine("www.google.com");
  private final AdresseIP ipGoogle = new AdresseIP("8.8.8.8");

  @Before
  public void setUp() throws IOException {
    List<String> lines = Arrays.asList(
        nomUvsq.getValeur() + " " + ipUvsq.getValeur(),
        nomEcampus.getValeur() + " " + ipEcampus.getValeur(),
        nomGoogle.getValeur() + " " + ipGoogle.getValeur()
    );
    Files.write(dbPath, lines);
  }

  @After
  public void tearDown() throws IOException {
    Files.deleteIfExists(dbPath);
  }

  @Test
  public void testConstructorLoadsData() throws IOException {
    Dns dns = new Dns();

    DnsItem item1 = dns.getItem(nomUvsq);
    assertNotNull("L'item www.uvsq.fr devrait être chargé", item1);
    assertEquals(ipUvsq, item1.getIp());

    DnsItem item2 = dns.getItem(ipGoogle);
    assertNotNull("L'item 8.8.8.8 devrait être chargé", item2);
    assertEquals(nomGoogle, item2.getNom());
  }

  @Test
  public void testGetItemNotFound() throws IOException {
    Dns dns = new Dns();

    assertNull(dns.getItem(new NomMachine("unknown.com")));

    assertNull(dns.getItem(new AdresseIP("1.2.3.4")));
  }

  @Test
  public void testGetItemsByDomaine() throws IOException {
    Dns dns = new Dns();

    List<DnsItem> uvsqItems = dns.getItems("uvsq.fr");
    assertEquals(2, uvsqItems.size());
    assertTrue(uvsqItems.contains(new DnsItem(nomUvsq, ipUvsq)));
    assertTrue(uvsqItems.contains(new DnsItem(nomEcampus, ipEcampus)));

    List<DnsItem> googleItems = dns.getItems("google.com");
    assertEquals(1, googleItems.size());
    assertEquals(nomGoogle, googleItems.get(0).getNom());

    List<DnsItem> unknownItems = dns.getItems("unknown.com");
    assertEquals(0, unknownItems.size());
  }

  @Test
  public void testAddItemSuccess() throws IOException {
    Dns dns = new Dns();

    NomMachine newNom = new NomMachine("pikachu.uvsq.fr");
    AdresseIP newIp = new AdresseIP("193.51.25.24");

    dns.addItem(newIp, newNom);

    DnsItem item = dns.getItem(newNom);
    assertNotNull("L'item ajouté doit exister en mémoire", item);
    assertEquals(newIp, item.getIp());

    List<String> lines = Files.readAllLines(dbPath);
    assertEquals("Le fichier BDD doit contenir 4 lignes", 4, lines.size());
    String expectedLine = "pikachu.uvsq.fr 193.51.25.24";
    assertTrue("Le fichier BDD doit contenir la nouvelle ligne", lines.contains(expectedLine));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItemDuplicateName() throws IOException {
    Dns dns = new Dns();
    dns.addItem(new AdresseIP("1.2.3.4"), nomUvsq);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddItemDuplicateIp() throws IOException {
    Dns dns = new Dns();
    dns.addItem(ipUvsq, new NomMachine("new.machine.com"));
  }

  @Test
  public void testLoadEmptyDatabase() throws IOException {
    Files.write(dbPath, Arrays.asList()); 
    
    Dns dns = new Dns();
    List<DnsItem> items = dns.getItems("uvsq.fr");
    assertEquals(0, items.size());
  }

  @Test
  public void testLoadMissingDatabase() throws IOException {
    Files.deleteIfExists(dbPath);

    Dns dns = new Dns();
    
    dns.addItem(ipGoogle, nomGoogle);
    
    assertTrue("Le fichier BDD aurait dû être créé", Files.exists(dbPath));
    List<String> lines = Files.readAllLines(dbPath);
    assertEquals(1, lines.size());
    assertEquals(nomGoogle.getValeur() + " " + ipGoogle.getValeur(), lines.get(0));
  }
}