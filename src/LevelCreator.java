import java.util.*;

public abstract class LevelCreator {
	public LevelCreator() { objs = new ArrayList<GameObject>(); }
	public abstract List<GameObject> makeObjects(Main m);
	public List<GameObject> objs;
	public void get(GameObject go) { objs.add(go); }
	public void get(List l) { for(Object go : l) get((GameObject)go); }
}