package fr.uvsq.cprog.collex;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NomMachineTest {

  @Test
  public void testConstructionValide() {
    NomMachine nom = new NomMachine("www.uvsq.fr");
    assertEquals("www.uvsq.fr", nom.getValeur());
  }

  @Test(expected = NullPointerException.class)
  public void testConstructionNull() {
    new NomMachine(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionVide() {
    new NomMachine("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructionSansPoint() {
    new NomMachine("machine");
  }

}
