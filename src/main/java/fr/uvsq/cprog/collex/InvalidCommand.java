package fr.uvsq.cprog.collex;

public final class InvalidCommand implements Commande {

  private final String errorMessage;

  public InvalidCommand(final String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    tui.affiche(this.errorMessage);
  }
}