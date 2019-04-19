import java.util.HashMap;
import java.util.TreeSet;

public class SortedSet {

  private TreeSet<String> sortedSet;

  public SortedSet(HashMap assessments) {
    setSortedSet(assessments);
  }

  public TreeSet<String> getSortedSet() {
    return sortedSet;
  }

  public void setSortedSet(HashMap assessments) {
    this.sortedSet = new TreeSet<>(assessments.keySet());
  }

  public String getlast()
  {
    return sortedSet.last();
  }
}
