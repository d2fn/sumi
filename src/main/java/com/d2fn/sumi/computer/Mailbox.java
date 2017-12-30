package com.d2fn.sumi.computer;

class Mailbox {

    private final String name;
    private final Object value;
    private final long time;

    public Mailbox(String name, Object value, long time) {
        this.name = name;
        this.value = value;
        this.time = time;
    }

    /**
     * @return the name of the value
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value described by name
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the clock value when this value was made available
     */
    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Mailbox{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", time=" + time +
                '}';
    }
}
