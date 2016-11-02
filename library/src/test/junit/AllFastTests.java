import org.junit.runner.RunWith;
import com.googlecode.junittoolbox.*;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*Test.class"})
@ExcludeCategories(testutil.Slow.class)

public class AllFastTests {

}
