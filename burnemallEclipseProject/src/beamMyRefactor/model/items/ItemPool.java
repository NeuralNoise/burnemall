package beamMyRefactor.model.items;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import beamMyRefactor.model.items.immaterial.Path;
import beamMyRefactor.model.items.material.AbstractPhotosensitive;
import beamMyRefactor.model.items.material.ItemHolder;
import beamMyRefactor.model.items.material.circular.Sootball;
import beamMyRefactor.model.pathing.PathManager;

@Root
public class ItemPool implements PathManager{

	@ElementList
	public List<AbstractItem> allItems = new ArrayList<>();
	
	public List<AbstractPhotosensitive> photosensitives = new ArrayList<>();
	public List<Path> paths = new ArrayList<>();
	public List<Sootball> sootballs = new ArrayList<>();
	public List<ItemHolder> holders = new ArrayList<>();
	

	public ItemPool(@ElementList(name="allItems")List<AbstractItem> items){
		for(AbstractItem i : items)
			register(i);
	}

	public ItemPool(){
	}
	
	public void register(AbstractItem item){
		register(item, null);
	}
	public void register(AbstractItem item, AbstractItem selected){
		assert item!=null;

		if(item instanceof ItemHolder)
			holders.add((ItemHolder)item);
		if(item instanceof Path)
			paths.add((Path)item);
		if(item instanceof Sootball)
			sootballs.add((Sootball)item);
		if(item instanceof AbstractPhotosensitive)
			photosensitives.add((AbstractPhotosensitive)item);
		
		if(selected != null && selected instanceof ItemHolder)
			((ItemHolder)selected).attach(item);
		else
			allItems.add(item);
	}
	
	
	public void unregister(AbstractItem item){
		assert item!=null;

		allItems.remove(item);
		for(ItemHolder h : holders)
			h.getItems().remove(h);
		holders.remove(item);
		photosensitives.remove(item);
		paths.remove(item);
		sootballs.remove(item);
	}
	
	public List<AbstractItem> getAllAndHolded(){
		List<AbstractItem> res = new ArrayList<>();
		res.addAll(allItems);
		for(ItemHolder h : holders)
			res.addAll(h.getItems());
		return res;
	}
	
	public void unregisterAllSootballs(){
		allItems.removeAll(sootballs);
		photosensitives.removeAll(sootballs);
		sootballs.clear();
	}
	
	@Override
	public int givePathID() {
		int id = 0;
		while(true){
			boolean available = true;
			for(Path path : paths)
				if(path.getID() == id){
					available = false;
					break;
				}
			if(available)
				return id;
			else
				id++;
		}
	}

	@Override
	public Path getPath(int id) {
		for(Path path : paths)
			if(path.getID() == id)
				return path;
		throw new RuntimeException("String with id "+id+" doesn't exist.");
	}

	
}
