package fr.uvsq.cprog.collex;

public final class GetIpCommand implements Commande {

  private final NomMachine nom;

  public GetIpCommand(final NomMachine nom) {
    this.nom = nom;
  }

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    DnsItem item = dns.getItem(this.nom);
    if (item != null) {
      tui.affiche(item.getIp().getValeur());
    } else {
      tui.affiche("ERREUR : Nom de machine inconnu.");
    }
  }
}