package fr.uvsq.cprog.collex;

package fr.uvsq.cprog;

import java.io.Closeable;
import java.util.Scanner;

public final class DnsTUI implements Closeable {

  private final Scanner scanner;

  public DnsTUI() {
    this.scanner = new Scanner(System.in);
  }

  public void affiche(final String resultat) {
    System.out.println(resultat);
  }

  public Commande nextCommande() {
    System.out.print("> ");

    String line = this.scanner.nextLine();

    if (line == null) {
      return new QuitCommand();
    }

    line = line.trim();

    if (line.isBlank()) {
      return new InvalidCommand("Commande vide. Saisissez 'quit' pour quitter.");
    }

    if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
      return new QuitCommand();
    }

    String[] parts = line.split("\\s+");
    String commandWord = parts[0];

    try {
      if (commandWord.equals("add")) {
        if (parts.length != 3) {
          return new InvalidCommand("Usage: add <ip> <nom.machine>");
        }
        AdresseIP ip = new AdresseIP(parts[1]);
        NomMachine nom = new NomMachine(parts[2]);
        return new AddCommand(ip, nom);
      }

      if (commandWord.equals("ls")) {
        if (parts.length == 2 && !parts[1].equals("-a")) {
          return new LsCommand(parts[1], false);
        }
        if (parts.length == 3 && parts[1].equals("-a")) {
          return new LsCommand(parts[2], true);
        }
        return new InvalidCommand("Usage: ls [-a] <domaine>");
      }

      if (parts.length == 1) {
        String input = parts[0];
        if (Character.isDigit(input.charAt(0))) {
          return new GetCommand(new AdresseIP(input));
        } else {
          return new GetCommand(new NomMachine(input));
        }
      }

      return new InvalidCommand("Commande inconnue : " + commandWord);

    } catch (IllegalArgumentException e) {
      return new InvalidCommand("Erreur de format : " + e.getMessage());
    }
  }

  @Override
  public void close() {
    this.scanner.close();
  }

}