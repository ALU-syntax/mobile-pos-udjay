package com.udjaya.kasirudjay.utils;

import android.util.Log;

import com.udjaya.kasirudjay.model.Customer;
import com.udjaya.kasirudjay.model.Outlet;
import com.udjaya.kasirudjay.model.Tax;
import com.udjaya.kasirudjay.model.TransactionItems;
import com.udjaya.kasirudjay.model.Transactions;
import com.udjaya.kasirudjay.model.User;

import java.util.List;

public class DebugUtils {

    private static final String TAG = "DEBUG_TRX";

    public static void debugTransaction(Transactions t) {
        if (t == null) {
            Log.d(TAG, "Transaction is null");
            return;
        }

        Log.d(TAG, "===== TRANSACTION =====");
        Log.d(TAG, "id: " + t.getId());
        Log.d(TAG, "outlet_id: " + t.getOutlet_id());
        Log.d(TAG, "user_id: " + t.getUser_id());
        Log.d(TAG, "customer_id: " + t.getCustomer_id());

        Log.d(TAG, "total: " + t.getTotal());
        Log.d(TAG, "nominal_bayar: " + t.getNominal_bayar());
        Log.d(TAG, "change: " + t.getChange());

        Log.d(TAG, "category_payment: " + t.getCategory_payment());
        Log.d(TAG, "tipe_pembayaran: " + t.getTipe_pembayaran());
        Log.d(TAG, "nama_tipe_pembayaran: " + t.getNama_tipe_pembayaran());

        Log.d(TAG, "total_pajak: " + t.getTotal_pajak());
        Log.d(TAG, "total_modifier: " + t.getTotal_modifier());
        Log.d(TAG, "total_diskon: " + t.getTotal_diskon());
        Log.d(TAG, "diskon_all_item: " + t.getDiskon_all_item());

        Log.d(TAG, "rounding_amount: " + t.getRounding_amount());
        Log.d(TAG, "tanda_rounding: " + t.getTanda_rounding());

        Log.d(TAG, "catatan: " + t.getCatatan());
        Log.d(TAG, "patty_cash_id: " + t.getPatty_cash_id());

        Log.d(TAG, "deleted_at: " + t.getDeleted_at());
        Log.d(TAG, "created_at: " + t.getCreated_at());
        Log.d(TAG, "updated_at: " + t.getUpdated_at());

        Log.d(TAG, "potongan_point: " + t.getPotongan_point());

        // Nested objects
        debugOutlet(t.getOutlet());
        debugUser(t.getUser());
        debugCustomer(t.getCustomer());
        debugTaxList(t.getTax());

        Log.d(TAG, "===== END TRANSACTION =====");
    }

    // ================= OUTLET =================

    public static void debugOutlet(Outlet outlet) {
        Log.d(TAG, "--- OUTLET ---");

        if (outlet == null) {
            Log.d(TAG, "outlet = null");
            return;
        }

        Log.d(TAG, "id: " + outlet.getId());
        Log.d(TAG, "name: " + outlet.getName());
        Log.d(TAG, "address: " + outlet.getAddress());
        Log.d(TAG, "phone: " + outlet.getPhone());
        Log.d(TAG, "created_at: " + outlet.getCreated_at());
        Log.d(TAG, "updated_at: " + outlet.getUpdated_at());
        Log.d(TAG, "catatan_nota: " + outlet.getCatatan_nota());

        User starter = outlet.getUser_started();
        if (starter != null) {
            Log.d(TAG, "--- OUTLET.user_started ---");
            debugUser(starter);
        } else {
            Log.d(TAG, "user_started: null");
        }
    }

    // ================= USER =================

    public static void debugUser(User user) {
        Log.d(TAG, "--- USER ---");

        if (user == null) {
            Log.d(TAG, "user = null");
            return;
        }

        Log.d(TAG, "id: " + user.getId());
        Log.d(TAG, "name: " + user.getName());
        Log.d(TAG, "username: " + user.getUsername());
        Log.d(TAG, "email: " + user.getEmail());
        Log.d(TAG, "status: " + user.getStatus());
        Log.d(TAG, "role: " + user.getRole());
        Log.d(TAG, "email_verified_at: " + user.getEmail_verified_at());
        // password biasanya nggak perlu di-log, tapi kalau untuk debug lokal boleh:
        Log.d(TAG, "password: " + user.getPassword());
        Log.d(TAG, "deleted: " + user.getDeleted());
        Log.d(TAG, "outlet_id: " + user.getOutlet_id());
        Log.d(TAG, "pin: " + user.getPin());
        Log.d(TAG, "remember_token: " + user.getRemember_token());
        Log.d(TAG, "created_at: " + user.getCreated_at());
        Log.d(TAG, "updated_at: " + user.getUpdated_at());
    }

    // ================= CUSTOMER =================

    public static void debugCustomer(Customer customer) {
        Log.d(TAG, "--- CUSTOMER ---");

        if (customer == null) {
            Log.d(TAG, "customer = null");
            return;
        }

        Log.d(TAG, "id: " + customer.getId());
        Log.d(TAG, "name: " + customer.getName());
        Log.d(TAG, "telfon: " + customer.getTelfon());
        Log.d(TAG, "umur: " + customer.getUmur());
        Log.d(TAG, "email: " + customer.getEmail());
        Log.d(TAG, "tanggal_lahir: " + customer.getTanggal_lahir());
        Log.d(TAG, "domisili: " + customer.getDomisili());
        Log.d(TAG, "gender: " + customer.getGender());
        Log.d(TAG, "community_id: " + customer.getCommunity_id());
        Log.d(TAG, "deleted_at: " + customer.getDeleted_at());
        Log.d(TAG, "created_at: " + customer.getCreated_at());
        Log.d(TAG, "updated_at: " + customer.getUpdated_at());
        Log.d(TAG, "exp: " + customer.getExp());
        Log.d(TAG, "point: " + customer.getPoint());
        Log.d(TAG, "referral_id: " + customer.getReferral_id());
        Log.d(TAG, "level_memberships_id: " + customer.getLevel_memberships_id());
    }

    // ================= TAX LIST =================

    public static void debugTaxList(List<Tax> taxList) {
        Log.d(TAG, "--- TAX LIST ---");

        if (taxList == null) {
            Log.d(TAG, "taxList = null");
            return;
        }

        Log.d(TAG, "taxList size: " + taxList.size());

        for (int i = 0; i < taxList.size(); i++) {
            Tax tax = taxList.get(i);
            debugTax(tax, i);
        }
    }

    public static void debugTax(Tax tax, int index) {
        Log.d(TAG, "--- TAX[" + index + "] ---");

        if (tax == null) {
            Log.d(TAG, "tax = null");
            return;
        }

        Log.d(TAG, "id: " + tax.getId());
        Log.d(TAG, "name: " + tax.getName());
        Log.d(TAG, "amount: " + tax.getAmount());
        Log.d(TAG, "satuan: " + tax.getSatuan());
        // kalau kamu punya getter outlet_id di Tax, bisa tambahkan:
        // Log.d(TAG, "outlet_id: " + tax.getOutlet_id());
    }

    public static void debugTransactionItemsList(List<TransactionItems> items) {
        Log.d(TAG, "--- TRANSACTION ITEMS LIST ---");

        if (items == null) {
            Log.d(TAG, "transactionItems list = null");
            return;
        }

        Log.d(TAG, "transactionItems size: " + items.size());

        for (int i = 0; i < items.size(); i++) {
            debugTransactionItems(items.get(i), i);
        }

        Log.d(TAG, "--- END TRANSACTION ITEMS LIST ---");
    }

    public static void debugTransactionItems(TransactionItems transactionItems, int index) {
        Log.d(TAG, "--- TRANSACTION ITEMS[" + index + "] ---");

        if (transactionItems == null) {
            Log.d(TAG, "transactionItems = null");
            return;
        }

        // Field utama
        Log.d(TAG, "id: " + transactionItems.getId());
        Log.d(TAG, "product_id: " + transactionItems.getProduct_id());
        Log.d(TAG, "variant_id: " + transactionItems.getVariant_id());
        Log.d(TAG, "discount_id: " + transactionItems.getDiscount_id());
        Log.d(TAG, "modifier_id: " + transactionItems.getModifier_id());
        Log.d(TAG, "sales_type_id: " + transactionItems.getSales_type_id());
        Log.d(TAG, "transaction_id: " + transactionItems.getTransaction_id());

        Log.d(TAG, "catatan: " + transactionItems.getCatatan());
        Log.d(TAG, "reward_item: " + transactionItems.getReward_item());

        Log.d(TAG, "deleted_at: " + transactionItems.getDeleted_at());
        Log.d(TAG, "created_at: " + transactionItems.getCreated_at());
        Log.d(TAG, "updated_at: " + transactionItems.getUpdated_at());

        Log.d(TAG, "total_count: " + transactionItems.getTotal_count());
        Log.d(TAG, "total_transaction: " + transactionItems.getTotal_transaction());

        // Nested: modifier (List<String>)
        List<String> modifierList = transactionItems.getModifier();
        if (modifierList == null) {
            Log.d(TAG, "modifier: null");
        } else if (modifierList.isEmpty()) {
            Log.d(TAG, "modifier: [] (empty)");
        } else {
            Log.d(TAG, "modifier size: " + modifierList.size());
            Log.d(TAG, "modifier values: " + modifierList.toString());
        }

        // Nested: product
        if (transactionItems.getProduct() != null) {
            Log.d(TAG, "--- TRANSACTION ITEMS[" + index + "].product ---");
            Log.d(TAG, "product: " + transactionItems.getProduct().toString());
            // kalau mau lebih detail, bisa bikin debugProduct(product) seperti debugOutlet/debugUser
        } else {
            Log.d(TAG, "product: null");
        }

        // Nested: variant
        if (transactionItems.getVariant() != null) {
            Log.d(TAG, "--- TRANSACTION ITEMS[" + index + "].variant ---");
            Log.d(TAG, "variant: " + transactionItems.getVariant().toString());
            // sama, bisa dibuatkan helper debugVariant(variant) kalau perlu
        } else {
            Log.d(TAG, "variant: null");
        }
    }

}
