package util;

import java.util.*;

public class VideoDemist implements IVideoDemist {
	protected static final int MISTY_THRESHOLD = 75;
	private VideoStatsService vss;
	private DemistService demistService;

	public List<Media> getMisty(List<Media> mediaList) {
		if (mediaList == null)
			return Collections.emptyList();

		List<Media> mistyVideos = new LinkedList<Media>();
		for (Media media : mediaList) {
			if ((media instanceof Video) && isMisty((Video) media))
				mistyVideos.add(media);
		}
		return mistyVideos;
	}

	private boolean isMisty(Video video) {
		VideoStats stats = vss.getStats(video.getFilename());
		return stats.getMistyScore() >= MISTY_THRESHOLD;
	}

	@Override
	public void demist(List<Media> mediaList) {
//		Collections.reverse(mediaList);
		for (Media media : mediaList) {
			demistService.demist(((Video) media)
					.getFilename());
		}
	}
}
