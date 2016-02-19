package com.sky.assignment.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "recommendation")
public class Recommendation {

    @XmlElement
    public final String uuid;

    @XmlElement
    public final Long start;

    @XmlElement
    public final Long end;

    private Recommendation() {
        this(null, null, null);
    }

    public Recommendation(String uuid, Long start, Long end) {
        this.uuid = uuid;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString(){
       return  String.format("uuid :%s  start:%d%n end:%d%n",uuid,start,end);
    }
}
