package cn.chainboard.ledgeryi.rbac;

import lombok.Builder;
import lombok.Data;

/**
 * @author Brian
 * @date 2021/3/10 20:11
 */
@Data
@Builder
public class NewNode {
    private String owner;
    private String host;
    private int port;
}
