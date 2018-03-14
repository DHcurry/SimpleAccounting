package io.github.skywalkerdarren.simpleaccounting.adapter;

import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.R;
import io.github.skywalkerdarren.simpleaccounting.model.Bill;
import io.github.skywalkerdarren.simpleaccounting.model.BillLab;

/**
 * Created by darren on 2018/2/12.
 *
 */

public class BillAdapter extends BaseMultiItemQuickAdapter<BillAdapter.BillInfo, BaseViewHolder> implements View.OnTouchListener {
    public static final int WITHOUT_REMARK = 0;
    public static final int WITH_REMARK = 1;
    public static final int HEADER = 2;
    private int mX;
    private int mY;


    public BillAdapter(List<BillInfo> bills) {
        super(bills);
        addItemType(WITH_REMARK, R.layout.list_bill_item);
        addItemType(WITHOUT_REMARK, R.layout.list_bill_item_without_remark);
        addItemType(HEADER, R.layout.list_bill_header);
    }

    public void setBills(List<BillInfo> bills) {
        setNewData(bills);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillInfo item) {
        switch (item.getItemType()) {
            case WITH_REMARK:
                helper.setText(R.id.remark_text_view, item.getRemark());
            case WITHOUT_REMARK:
                helper.setTextColor(R.id.balance_edit_text, item.isExpense() ?
                        Color.rgb(0xFF, 0x45, 0x00) :
                        Color.rgb(0xAD, 0xFF, 0x2F));
                helper.setImageResource(R.id.type_image_view, item.getBillTypeResId());
                helper.setText(R.id.title_text_view, item.getTitle());
                helper.setText(R.id.balance_edit_text, item.getBalance());
                helper.addOnClickListener(R.id.content_card_view);
                helper.addOnClickListener(R.id.image_card_view);
                helper.addOnLongClickListener(R.id.content_card_view);
                helper.addOnLongClickListener(R.id.image_card_view);
                helper.setAlpha(R.id.bill_item, 0);
                helper.setOnTouchListener(R.id.content_card_view, this);
                helper.setOnTouchListener(R.id.image_card_view, this);
                break;
            case HEADER:
                helper.setText(R.id.bills_date_text_view, item.getDateTime().toString("yyyy-MM-dd"));
                helper.setText(R.id.bill_expense_text_view, item.getExpense());
                helper.setText(R.id.bill_income_text_view, item.getIncome());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mX = (int) motionEvent.getRawX();
        mY = (int) motionEvent.getRawY();
        return false;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public static class BillInfo implements MultiItemEntity {
        private int mType;

        private UUID mUUID;
        private String mTitle;
        private String mRemark;
        private String mBalance;
        private boolean mIsExpense;
        private String mBillTypeName;
        @DrawableRes
        private int mBillTypeResId;

        private String mIncome;
        private String mExpense;

        private DateTime mDateTime;

        public BillInfo(Bill bill) {
            mType = TextUtils.isEmpty(bill.getRemark()) ? WITHOUT_REMARK : WITH_REMARK;
            mUUID = bill.getId();
            mTitle = bill.getName();
            mRemark = bill.getRemark();
            mBalance = bill.getBalance().toString();
            mIsExpense = bill.isExpense();
            mBillTypeName = bill.getTypeName();
            mBillTypeResId = bill.getTypeResId();
            mDateTime = bill.getDate();
        }

        public BillInfo(DateHeaderDivider header) {
            mType = HEADER;
            mDateTime = header.getDate();
            mExpense = header.getExpense();
            mIncome = header.getIncome();
        }

        public static List<BillInfo> getBillInfoList(List<Bill> bills, BillLab billLab) {
            List<BillInfo> billInfoList = new ArrayList<>();
            // 上一个账单的年月日
            DateTime date = null;
            for (int i = 0; i < bills.size(); i++) {
                Bill bill = bills.get(i);
                DateTime dateTime = bill.getDate();
                int y = dateTime.getYear();
                int m = dateTime.getMonthOfYear();
                int d = dateTime.getDayOfMonth();
                // 当前账单的年月日
                DateTime currentDate = new DateTime(y, m, d, 0, 0);
                // 如果当前帐单与上一张单年月日不同，则添加账单
                if (date == null || !date.equals(currentDate)) {
                    date = currentDate;
                    BigDecimal income = billLab.getStats(date, date.plusDays(1)).getIncome();
                    BigDecimal expense = billLab.getStats(date, date.plusDays(1)).getExpense();
                    billInfoList.add(new BillInfo(new DateHeaderDivider(date, income, expense)));
                }
                billInfoList.add(new BillInfo(bill));

            }
            return billInfoList;
        }

        @Override
        public int getItemType() {
            return mType;
        }

        public String getRemark() {
            return mRemark;
        }

        @DrawableRes
        public int getBillTypeResId() {
            return mBillTypeResId;
        }

        public String getBillTypeName() {
            return mBillTypeName;
        }

        public UUID getUUID() {
            return mUUID;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getBalance() {
            return mBalance;
        }

        public boolean isExpense() {
            return mIsExpense;
        }

        public String getIncome() {
            return mIncome;
        }

        public String getExpense() {
            return mExpense;
        }

        public DateTime getDateTime() {
            return mDateTime;
        }

    }

}
