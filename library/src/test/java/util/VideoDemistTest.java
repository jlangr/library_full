package util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.junit.*;

@RunWith(MockitoJUnitRunner.class)
public class VideoDemistTest {
	@InjectMocks
	VideoDemist videoDemist;
	@Mock
	private VideoStatsService videoStatsService;
	@Mock
	private DemistService demistService;
	
	@Test
	public void returnEmptyOnNullList() {
		assertThat(videoDemist.getMisty(null), 
				is(equalTo(Collections.<Media>emptyList())));
	}

	@Test
	public void doesNotIncludeNonVideos() {
		List<Media> listWithoutVideo = Arrays.<Media>asList(new Image());
		
		List<Media> misty = videoDemist.getMisty(listWithoutVideo);
		
		assertThat(misty, is(equalTo(Collections.<Media>emptyList())));
	}

	@Test
	public void doesIncludeMistyVideos() {
		List<Media> listWithVideo = Arrays.<Media>asList(new Video());
		when(videoStatsService.getStats(anyString()))
			.thenReturn(new VideoStats(VideoDemist.MISTY_THRESHOLD + 1)) ;

		List<Media> misty = videoDemist.getMisty(listWithVideo);
		
        assertThat(misty, is(equalTo(listWithVideo)));
	}
	
	@Test
	public void doesNotIncludeNonMistyVideo() {
        List<Media> listWithNonMistyVideo = Arrays.<Media>asList(new Video());
        when(videoStatsService.getStats(anyString()))
        		.thenReturn(new VideoStats(VideoDemist.MISTY_THRESHOLD - 1));

        List<Media> misty = videoDemist.getMisty(listWithNonMistyVideo);
        
		assertThat(misty, is(equalTo(Collections.<Media>emptyList())));
	}
	
	@Test
	public void includesOnlyVideoThatAreMist() {
		Video nonMistyVideo = new Video("nonmisty.mp4");
		Video mistyVideo = new Video("misty.mp4");
        List<Media> list = Arrays.<Media>asList(nonMistyVideo, mistyVideo);
        when(videoStatsService.getStats("misty.mp4"))
        		.thenReturn(new VideoStats(VideoDemist.MISTY_THRESHOLD + 1));
        when(videoStatsService.getStats("nonmisty.mp4"))
        		.thenReturn(new VideoStats(VideoDemist.MISTY_THRESHOLD - 1));

        List<Media> misty = videoDemist.getMisty(list);
        
		assertThat(misty, is(equalTo(Arrays.<Video>asList(mistyVideo))));
	}
	
	@Test
	public void demistsMistyVideos() {
		Video mistyVideo = new Video("misty.mp4");
		Video mistyVideo2 = new Video("misty2.mp4");
        List<Media> list = Arrays.<Media>asList(mistyVideo, mistyVideo2);
//        InOrder inorder = inOrder(demistService);

        videoDemist.demist(list);
        
        verify(demistService, times(2)).demist(anyString());
        
//        inorder.verify(demistService).demist("misty2.mp4");
//        inorder.verify(demistService).demist("misty.mp4");
        
	}
}
