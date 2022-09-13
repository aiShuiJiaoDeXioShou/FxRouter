package lh.router.entity;

import lh.router.annotation.FXRoute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FxRouteBean {
    private FXRoute fxRoute;
    private Class<?> aClass;
    private String className;
    private List<FxRouteBean> children;
}
