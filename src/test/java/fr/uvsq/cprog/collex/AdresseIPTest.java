package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdresseIPTest {

  @Test
  public void testConstructionValide() {
    AdresseIP ip = new AdresseIP("192.168.1.1");
    assertEquals("192.168.1.1", ip.getValeur());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructionNull() {
    new AdresseIP(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionVide() {
    new AdresseIP("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionBlanche() {
    new AdresseIP("   ");
  }

}
