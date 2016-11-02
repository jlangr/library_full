import org.junit.runner.RunWith;
import com.googlecode.junittoolbox.*;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*Test.class"})
@IncludeCategories(testutil.Slow.class)
public class AllSlowTests {

}
