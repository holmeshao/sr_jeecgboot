package org.jeecg.modules.workflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class NodeUiSchema implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nodeId;
    private List<Component> components = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class Component implements Serializable {
        private static final long serialVersionUID = 1L;

        private String key;
        private String type;
        private String label;
        private Boolean required;
        private Boolean readonly;
        private Boolean hidden;
        private String propsJson; // 透传组件属性JSON
        private Integer order;
    }
}


