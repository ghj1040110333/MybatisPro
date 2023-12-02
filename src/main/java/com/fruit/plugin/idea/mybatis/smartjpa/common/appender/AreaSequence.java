package com.fruit.plugin.idea.mybatis.smartjpa.common.appender;

/**
 * The enum Area sequence.
 */
public enum AreaSequence {
    /**
     * Un known area sequence.
     */
    UN_KNOWN(-100),
    /**
     * Result area sequence.
     */
    RESULT(10),
    /**
     * Condition area sequence.
     */
    CONDITION(20),
    /**
     * Sort area sequence.
     */
    SORT(30),
    /**
     * Area area sequence.
     */
    AREA(100);
    /**
     * 优先级
     */
    private int sequence;

    /**
     * Gets sequence.
     *
     * @return the sequence
     */
    public int getSequence() {
        return this.sequence;
    }

    AreaSequence(int sequence) {

        this.sequence = sequence;
    }
}
