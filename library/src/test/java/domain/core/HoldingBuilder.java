package domain.core;

import com.loc.material.api.Material;

public class HoldingBuilder {
    private Material material = new Material("1", "", "1", "", "");
    private int copyNumber = 1;
    private Branch branch = Branch.CHECKED_OUT;

    public HoldingBuilder with(Material material) {
        this.material = material;
        return this;
    }

    public HoldingBuilder withClassification(String classification) {
        this.material = new Material(classification, "", classification, "", "");
        return this;
    }

    public HoldingBuilder withCopy(int copyNumber) {
        this.copyNumber = copyNumber;
        return this;
    }

    public HoldingBuilder atBranch(Branch branch) {
        this.branch = branch;
        return this;
    }

    public Holding create() {
        return new Holding(material, branch, copyNumber);
    }
}