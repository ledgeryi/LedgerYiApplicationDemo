package cn.chainboard.ledgeryi.app.erc;

import lombok.Data;

@Data
public class TokenInfo {
    /**
     * 合约名称
     */
    private String name;
    /**
     * 代币简称
     */
    private String symbol;
    /**
     * 初始发行总量
     */
    private long totalSupply;

    /**
     * 精度
     */
    private int decimals;

    public TokenInfo(String name, String symbol, long totalSupply) {
        this.name = name;
        this.symbol = symbol;
        this.totalSupply = totalSupply;
    }

    public TokenInfo(String name, String symbol, long totalSupply, int decimals) {
        this.name = name;
        this.symbol = symbol;
        this.totalSupply = totalSupply;
        this.decimals = decimals;
    }
}
