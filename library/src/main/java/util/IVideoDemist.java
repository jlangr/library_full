package util;

import java.util.*;

public interface IVideoDemist {
	List<Media> getMisty(List<Media> mediaList);
	void demist(List<Media> mediaList);
//	void demistMistyVideos(List<Video> mistyVideos);
}
