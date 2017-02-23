import com.googlecode.junittoolbox.ExcludeCategories;
import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import testutil.MetaJunit;

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({"**/*.class"})
@ExcludeCategories({testutil.Slow.class, MetaJunit.class})

@Category(MetaJunit.class)
public class AllFastTests {

}
