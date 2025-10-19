package fr.uvsq.cprog.collex;

public final class QuitCommand implements Commande {

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    tui.affiche("Ciao !");
  }
}