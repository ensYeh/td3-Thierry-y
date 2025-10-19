package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertTrue;

import org.junit.Before;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DnsItemTest {

  private AdresseIP ip;
  private NomMachine nom;
  private DnsItem item;

  @Before
  public void setUp() {
    ip = new AdresseIP("193.51.31.90");
    nom = new NomMachine("www.uvsq.fr");
    item = new DnsItem(nom, ip);
  }

  @Test
  public void testConstructionEtGetters() {
    assertEquals(nom, item.getNom());
    assertEquals(ip, item.getIp());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructionNomNull() {
    new DnsItem(null, ip);
  }

  @Test(expected = NullPointerException.class)
  public void testConstructionIpNull() {
    new DnsItem(nom, null);
  }

}