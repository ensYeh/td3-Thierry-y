package fr.uvsq.cprog.collex;

public final class GetNameCommand implements Commande {

  private final AdresseIP ip;

  public GetNameCommand(final AdresseIP ip) {
    this.ip = ip;
  }

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    DnsItem item = dns.getItem(this.ip);
    if (item != null) {
      tui.affiche(item.getNom().getValeur());
    } else {
      tui.affiche("ERREUR : Adresse IP inconnue.");
    }
  }
}
