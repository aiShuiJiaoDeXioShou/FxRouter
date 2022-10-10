package lh.router.entity;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class RouteContext {
    private ArrayList<Object> query;
    private Object params;
}
