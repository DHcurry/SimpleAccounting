package io.github.skywalkerdarren.simpleaccounting.model.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BillDTO {
    private UUID typeId;
    private UUID accountId;
    private String date;
    private String name;
    private BigDecimal balance;
    private String remark;
    private UUID uuid;

    public BillDTO() {
    }

    public BillDTO(Bill bill) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        typeId = bill.getTypeId();
        accountId = bill.getAccountId();
        date = format.format(bill.getDate().toDate());
        name = bill.getName();
        balance = bill.getBalance();
        remark = bill.getRemark();
        uuid = bill.getUuid();
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
