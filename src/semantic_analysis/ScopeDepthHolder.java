package semantic_analysis;

public class ScopeDepthHolder implements SemanticAnalyser {
    private int depth = 0;

    private boolean maxDepthEnabled = false;
    private int maxDepth = 1;

    private boolean minDepthEnabled = false;
    private int minDepth = 0;

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void increaseDepth() {
        depth++;

        if(maxDepthEnabled && depth > maxDepth) {
            throw new MaximumDepthExcededException(maxDepth);
        }
    }

    public void decreaseDepth() {
        depth--;

        if((!minDepthEnabled && depth < 0) ||
            (minDepthEnabled && depth < minDepth)
        ) {
            throw new MinimumDepthExcededException(minDepth);
        }
    }

    public int getDepth() {
        return depth;
    }


    /**
     * Sets the maximum allowed depth
     * This will not enable the maximum depth
     * @param maxDepth
     */
    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void enableMaxDepth() {
        maxDepthEnabled = true;
    }

    public void disableMaxDepth() {
        maxDepthEnabled = true;
    }


    /**
     * Sets the minimum allowed depth
     * This will not enable the minimum depth
     * @param minDepth
     */
    public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
    }

    public void enableMinDepth() {
        minDepthEnabled = true;
    }

    public void disableMinDepth() {
        minDepthEnabled = true;
    }


    public class MaximumDepthExcededException extends RuntimeException {

        public MaximumDepthExcededException(int maxDepth) {
            super("The maximum depth of " + Integer.toString(maxDepth) + " was exceded");
        }
    }

    public class MinimumDepthExcededException extends RuntimeException {

        public MinimumDepthExcededException(int minDepth) {
            super("The minumum depth of " + Integer.toString(minDepth) + " was exceded");
        }
    }
}
