package api.library;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.Material;
import domain.core.ClassificationApiFactory;
import domain.core.Holding;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HoldingService_FindByBranchTest {
    private HoldingService service = new HoldingService();
    private ClassificationApi classificationApi = mock(ClassificationApi.class);
    private BranchService branchService = new BranchService();

    @Before
    public void initialize() {
        LibraryData.deleteAll();
        ClassificationApiFactory.setService(classificationApi);
    }

    private String addHolding(String sourceId, String branchScanCode) {
        when(classificationApi.retrieveMaterial(sourceId)).thenReturn(new Material(sourceId, "", "", "", ""));
        return service.add(sourceId, branchScanCode);
    }

    @Test
    public void returnsOnlyHoldingsAtBranch() {
        String branchAScanCode = branchService.add("branch A");
        String branchBScanCode = new BranchService().add("branch B");
        addHolding("123", branchAScanCode);
        addHolding("456", branchAScanCode);
        addHolding("999", branchBScanCode);

        List<Holding> holdings = service.findByBranch(branchAScanCode);

        List<String> holdingSourceIds = holdings.stream()
                .map(h -> h.getMaterial().getSourceId())
                .collect(Collectors.toList());
        assertThat(holdingSourceIds, equalTo(asList("123", "456")));
    }
}
