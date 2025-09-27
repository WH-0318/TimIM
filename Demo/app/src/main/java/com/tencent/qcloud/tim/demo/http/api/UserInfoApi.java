package com.tencent.qcloud.tim.demo.http.api;

import androidx.annotation.NonNull;

import com.hjq.http.config.IRequestApi;

import java.util.List;

public final class UserInfoApi implements IRequestApi {

    @NonNull
    @Override
    public String getApi() {
        return "user/info";
    }

    public class Bean {

        private int id;
        private String username;
        private String nickName;
        private String avatar;
        private double usdtBalance;
        private double trxBalance;
        private double fuel;
        private double insuranceBalance;
        private double freezeInsuranceBalance;
        private String email;
        private boolean setTransactionPassword;
        private String withdrawalTrxAddress;
        private String trxToUsdtRate;
        private String usdtToTrxRate;
        private String withdrawalCommissionRate;
        private String transferCommissionRate;
        private int totalTeamUser;
        private int todayTeamUser;
        private Integer allLevelTotalTeamUser;
        private double todayGameWater;
        private double yesterdayGameWater;
        private double historyGameWater;
        private Double yesterdayTeamWater;
        private Double todayTeamWater;
        private String teamEarningRate;
        private double fuelToRmb;
        private boolean isRecharged;
        private String usdToRmbRate;
        private double stockOptionsBalance;
        private double freeCommissionBalance;
        private int isAuth;
        private String alipayName;
        private String alipayAccount;
        private String bankName;
        private String bankUserName;
        private String bankCardNo;
        private double benefitAmount;
        private boolean tk;
        private int inviteCode;
        private int isFaceAuth;
        private String faceAuthStatus;
        private String cardNo;
        private String name;
        private String idcard;
        private int isBind;
        private String imAccountUsername;
        private String imUserSig;
        private double integral;
        private double minWithdrawalAmount;
        private double maxWithdrawalAmount;
        private double commissionRate;
        private String idImage;
        private String idStatus;
        private String rejectReason;
        private boolean isAdmin;
        private List<Object> children;

        // --- Getter & Setter ---
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getNickName() { return nickName; }
        public void setNickName(String nickName) { this.nickName = nickName; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }

        public double getUsdtBalance() { return usdtBalance; }
        public void setUsdtBalance(double usdtBalance) { this.usdtBalance = usdtBalance; }

        public double getTrxBalance() { return trxBalance; }
        public void setTrxBalance(double trxBalance) { this.trxBalance = trxBalance; }

        public double getFuel() { return fuel; }
        public void setFuel(double fuel) { this.fuel = fuel; }

        public double getInsuranceBalance() { return insuranceBalance; }
        public void setInsuranceBalance(double insuranceBalance) { this.insuranceBalance = insuranceBalance; }

        public double getFreezeInsuranceBalance() { return freezeInsuranceBalance; }
        public void setFreezeInsuranceBalance(double freezeInsuranceBalance) { this.freezeInsuranceBalance = freezeInsuranceBalance; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public boolean isSetTransactionPassword() { return setTransactionPassword; }
        public void setSetTransactionPassword(boolean setTransactionPassword) { this.setTransactionPassword = setTransactionPassword; }

        public String getWithdrawalTrxAddress() { return withdrawalTrxAddress; }
        public void setWithdrawalTrxAddress(String withdrawalTrxAddress) { this.withdrawalTrxAddress = withdrawalTrxAddress; }

        public String getTrxToUsdtRate() { return trxToUsdtRate; }
        public void setTrxToUsdtRate(String trxToUsdtRate) { this.trxToUsdtRate = trxToUsdtRate; }

        public String getUsdtToTrxRate() { return usdtToTrxRate; }
        public void setUsdtToTrxRate(String usdtToTrxRate) { this.usdtToTrxRate = usdtToTrxRate; }

        public String getWithdrawalCommissionRate() { return withdrawalCommissionRate; }
        public void setWithdrawalCommissionRate(String withdrawalCommissionRate) { this.withdrawalCommissionRate = withdrawalCommissionRate; }

        public String getTransferCommissionRate() { return transferCommissionRate; }
        public void setTransferCommissionRate(String transferCommissionRate) { this.transferCommissionRate = transferCommissionRate; }

        public int getTotalTeamUser() { return totalTeamUser; }
        public void setTotalTeamUser(int totalTeamUser) { this.totalTeamUser = totalTeamUser; }

        public int getTodayTeamUser() { return todayTeamUser; }
        public void setTodayTeamUser(int todayTeamUser) { this.todayTeamUser = todayTeamUser; }

        public Integer getAllLevelTotalTeamUser() { return allLevelTotalTeamUser; }
        public void setAllLevelTotalTeamUser(Integer allLevelTotalTeamUser) { this.allLevelTotalTeamUser = allLevelTotalTeamUser; }

        public double getTodayGameWater() { return todayGameWater; }
        public void setTodayGameWater(double todayGameWater) { this.todayGameWater = todayGameWater; }

        public double getYesterdayGameWater() { return yesterdayGameWater; }
        public void setYesterdayGameWater(double yesterdayGameWater) { this.yesterdayGameWater = yesterdayGameWater; }

        public double getHistoryGameWater() { return historyGameWater; }
        public void setHistoryGameWater(double historyGameWater) { this.historyGameWater = historyGameWater; }

        public Double getYesterdayTeamWater() { return yesterdayTeamWater; }
        public void setYesterdayTeamWater(Double yesterdayTeamWater) { this.yesterdayTeamWater = yesterdayTeamWater; }

        public Double getTodayTeamWater() { return todayTeamWater; }
        public void setTodayTeamWater(Double todayTeamWater) { this.todayTeamWater = todayTeamWater; }

        public String getTeamEarningRate() { return teamEarningRate; }
        public void setTeamEarningRate(String teamEarningRate) { this.teamEarningRate = teamEarningRate; }

        public double getFuelToRmb() { return fuelToRmb; }
        public void setFuelToRmb(double fuelToRmb) { this.fuelToRmb = fuelToRmb; }

        public boolean isRecharged() { return isRecharged; }
        public void setRecharged(boolean recharged) { isRecharged = recharged; }

        public String getUsdToRmbRate() { return usdToRmbRate; }
        public void setUsdToRmbRate(String usdToRmbRate) { this.usdToRmbRate = usdToRmbRate; }

        public double getStockOptionsBalance() { return stockOptionsBalance; }
        public void setStockOptionsBalance(double stockOptionsBalance) { this.stockOptionsBalance = stockOptionsBalance; }

        public double getFreeCommissionBalance() { return freeCommissionBalance; }
        public void setFreeCommissionBalance(double freeCommissionBalance) { this.freeCommissionBalance = freeCommissionBalance; }

        public int getIsAuth() { return isAuth; }
        public void setIsAuth(int isAuth) { this.isAuth = isAuth; }

        public String getAlipayName() { return alipayName; }
        public void setAlipayName(String alipayName) { this.alipayName = alipayName; }

        public String getAlipayAccount() { return alipayAccount; }
        public void setAlipayAccount(String alipayAccount) { this.alipayAccount = alipayAccount; }

        public String getBankName() { return bankName; }
        public void setBankName(String bankName) { this.bankName = bankName; }

        public String getBankUserName() { return bankUserName; }
        public void setBankUserName(String bankUserName) { this.bankUserName = bankUserName; }

        public String getBankCardNo() { return bankCardNo; }
        public void setBankCardNo(String bankCardNo) { this.bankCardNo = bankCardNo; }

        public double getBenefitAmount() { return benefitAmount; }
        public void setBenefitAmount(double benefitAmount) { this.benefitAmount = benefitAmount; }

        public boolean isTk() { return tk; }
        public void setTk(boolean tk) { this.tk = tk; }

        public int getInviteCode() { return inviteCode; }
        public void setInviteCode(int inviteCode) { this.inviteCode = inviteCode; }

        public int getIsFaceAuth() { return isFaceAuth; }
        public void setIsFaceAuth(int isFaceAuth) { this.isFaceAuth = isFaceAuth; }

        public String getFaceAuthStatus() { return faceAuthStatus; }
        public void setFaceAuthStatus(String faceAuthStatus) { this.faceAuthStatus = faceAuthStatus; }

        public String getCardNo() { return cardNo; }
        public void setCardNo(String cardNo) { this.cardNo = cardNo; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getIdcard() { return idcard; }
        public void setIdcard(String idcard) { this.idcard = idcard; }

        public int getIsBind() { return isBind; }
        public void setIsBind(int isBind) { this.isBind = isBind; }

        public String getImAccountUsername() { return imAccountUsername; }
        public void setImAccountUsername(String imAccountUsername) { this.imAccountUsername = imAccountUsername; }

        public String getImUserSig() { return imUserSig; }
        public void setImUserSig(String imUserSig) { this.imUserSig = imUserSig; }

        public double getIntegral() { return integral; }
        public void setIntegral(double integral) { this.integral = integral; }

        public double getMinWithdrawalAmount() { return minWithdrawalAmount; }
        public void setMinWithdrawalAmount(double minWithdrawalAmount) { this.minWithdrawalAmount = minWithdrawalAmount; }

        public double getMaxWithdrawalAmount() { return maxWithdrawalAmount; }
        public void setMaxWithdrawalAmount(double maxWithdrawalAmount) { this.maxWithdrawalAmount = maxWithdrawalAmount; }

        public double getCommissionRate() { return commissionRate; }
        public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

        public String getIdImage() { return idImage; }
        public void setIdImage(String idImage) { this.idImage = idImage; }

        public String getIdStatus() { return idStatus; }
        public void setIdStatus(String idStatus) { this.idStatus = idStatus; }

        public String getRejectReason() { return rejectReason; }
        public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

        public boolean isAdmin() { return isAdmin; }
        public void setAdmin(boolean admin) { isAdmin = admin; }

        public List<Object> getChildren() { return children; }
        public void setChildren(List<Object> children) { this.children = children; }
    }


}