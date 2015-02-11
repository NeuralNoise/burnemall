package beamMyRefactor.model.pathing;

import beamMyRefactor.model.items.immaterial.Path;

public interface PathManager {

	public int givePathID();
	public Path getPath(int ID);
}
