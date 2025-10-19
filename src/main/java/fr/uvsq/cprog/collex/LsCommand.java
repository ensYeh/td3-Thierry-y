package fr.uvsq.cprog.collex;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public final class LsCommand implements Commande {

  private final String domaine;
  private final boolean sortByIp;

  public LsCommand(final String domaine, final boolean sortByIp) {
    this.domaine = domaine;
    this.sortByIp = sortByIp;
  }

  @Override
  public void execute(final Dns dns, final DnsTUI tui) {
    List<DnsItem> items = dns.getItems(this.domaine);

    if (items.isEmpty()) {
      tui.affiche("Aucune machine trouvée pour le domaine : " + this.domaine);
      return;
    }

    Comparator<DnsItem> comparator;
    if (this.sortByIp) {
      comparator = Comparator.comparing(item -> item.getIp().getValeur());
    } else {
      comparator = Comparator.comparing(item -> item.getNom().getValeur());
    }

    String resultat = items.stream()
        .sorted(comparator)
        .map(DnsItem::formatForLs)
        .collect(Collectors.joining("\n"));

    tui.affiche(resultat);
  }
}