package io.github.skywalkerdarren.simpleaccounting.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.math.BigDecimal;
import java.util.UUID;

import io.github.skywalkerdarren.simpleaccounting.model.DbSchema.AccountTable.Cols;
import io.github.skywalkerdarren.simpleaccounting.model.entity.Account;

/**
 * @author darren
 * @date 2018/4/2
 */

class AccountCursorWrapper extends CursorWrapper {
    AccountCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    Account getAccount() {
        return new Account(UUID.fromString(getString(getColumnIndex(Cols.UUID))))
                .setName(getString(getColumnIndex(Cols.NAME)))
                .setBalanceHint(getString(getColumnIndex(Cols.BALANCE_HINT)))
                .setBalance(new BigDecimal(getString(getColumnIndex(Cols.BALANCE))))
                .setBitmap(getString(getColumnIndex(Cols.IMAGE)))
                .setColorId(getInt(getColumnIndex(Cols.COLOR_ID)));
    }
}
