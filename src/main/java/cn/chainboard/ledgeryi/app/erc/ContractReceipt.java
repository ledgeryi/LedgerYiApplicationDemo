package cn.chainboard.ledgeryi.app.erc;

import cn.ledgeryi.protos.Protocol.Transaction.Result.ContractResult;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContractReceipt {
    /**
     * 交易执行结果，true：执行成功，false：执行失败
     */
    private boolean execResult;

    /**
     * 交易执行结果状态“：
     * DEFAULT = 0;
     * UCCESS = 1;
     * REVERT = 2;
     * BAD_JUMP_DESTINATION = 3;
     * PRECOMPILED_CONTRACT = 4;
     * STACK_TOO_SMALL = 5;
     * STACK_TOO_LARGE = 6;
     * ILLEGAL_OPERATION = 7;
     * STACK_OVERFLOW = 8;
     * JVM_STACK_OVER_FLOW = 9;
     * UNKNOWN = 10;
     */
    private ContractResult contractResult;

    /**
     * 交易执行结果失败原因
     */
    private String reason;
}
