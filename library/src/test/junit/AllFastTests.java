import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import com.googlecode.junittoolbox.*;
import testutil.MetaJunit;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*.class"})
@ExcludeCategories({testutil.Slow.class,MetaJunit.class})

@Category(MetaJunit.class)
public class AllFastTests {

}
