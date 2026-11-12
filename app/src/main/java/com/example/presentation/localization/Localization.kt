package com.example.presentation.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val code: String, val label: String) {
    ENGLISH("en", "English"),
    AMHARIC("am", "አማርኛ")
}

class LocalizationManager(initialLanguage: AppLanguage = AppLanguage.ENGLISH) {
    var currentLanguage = mutableStateOf(initialLanguage)

    fun toggleLanguage() {
        currentLanguage.value = if (currentLanguage.value == AppLanguage.ENGLISH) {
            AppLanguage.AMHARIC
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun isAmharic(): Boolean = currentLanguage.value == AppLanguage.AMHARIC
}

val LocalAppLanguage = staticCompositionLocalOf { LocalizationManager() }

object Loc {
    fun appTitle(isAm: Boolean) = if (isAm) "ETB verify • የኢትዮጵያ ብር ማረጋገጫ" else "ETB verify"
    fun appSubtitle(isAm: Boolean) = if (isAm) "የኢትዮጵያ ብር የጸጥታና ትክክለኛነት ማረጋገጫ ስርዓት" else "National Currency Verification & Intelligence"
    fun federalSuite(isAm: Boolean) = if (isAm) "ብሔራዊ የፍተሻ ስርዓት" else "FEDERAL INSPECTION SUITE"
    fun nbeCore(isAm: Boolean) = if (isAm) "የኢትዮጵያ ብሔራዊ ባንክ AI" else "NBE-AI CORE 4.2"
    fun publicScanner(isAm: Boolean) = if (isAm) "የህዝብ ብር ማረጋገጫ ስካነር" else "ETB verify Public Scanner"
    fun noAccountReq(isAm: Boolean) = if (isAm) "ፈጣን የብር ማረጋገጫ • መለያ አያስፈልግም" else "Instant AI Banknote Authentication • No Account Required"
    fun freeService(isAm: Boolean) = if (isAm) "ነፃ የህዝብ አገልግሎት • ለሁሉም ክፍት" else "Free Public Service • Open for all citizens & merchants"
    
    // Bottom Navigation
    fun navHome(isAm: Boolean) = if (isAm) "መነሻ" else "Home"
    fun navScan(isAm: Boolean) = if (isAm) "ቃኝ" else "Scan"
    fun navMap(isAm: Boolean) = if (isAm) "የማጭበርበር ካርታ" else "Fraud Map"
    fun navAlerts(isAm: Boolean) = if (isAm) "ማስጠንቀቂያዎች" else "Alerts"
    fun navProfile(isAm: Boolean) = if (isAm) "ኢንተሊጀንስ" else "Intelligence"

    // Dashboard Actions
    fun snapScanTitle(isAm: Boolean) = if (isAm) "ፎቶ አንስተው በብርሃን ይመርምሩ" else "Snap & Scan with Flashlight"
    fun snapScanDesc(isAm: Boolean) = if (isAm) "የ 100 እና 200 ብር ኖቶችን ፈጣን በሆነ የካሜራ AI ያረጋግጡ" else "Instant camera verification for 100 & 200 ETB banknotes. Hold note steady in any lighting."
    fun reportIncident(isAm: Boolean) = if (isAm) "አስመሳይ ብር ሪፖርት ያድርጉ" else "Report Incident"
    fun reportIncidentSub(isAm: Boolean) = if (isAm) "የተጠረጠረ ብር ከቦታው መረጃ ጋር" else "Submit forged note with real-time GPS tag"
    fun fraudMap(isAm: Boolean) = if (isAm) "የአደጋ ካርታ" else "Fraud Map"
    fun fraudMapSub(isAm: Boolean) = if (isAm) "የአዲስ አበባ አደጋ ቀጠናዎች ራዳር" else "Live Addis Ababa hotspot radar & telemetry"
    fun auditArchive(isAm: Boolean) = if (isAm) "የተመረመሩ ማህደር" else "Audit Archive"
    fun auditArchiveSub(isAm: Boolean) = if (isAm) "34 በምስጠራ የተረጋገጡ ፍተሻዎች" else "34 Cryptographically Signed Scans"
    fun viewAll(isAm: Boolean) = if (isAm) "ሁሉንም እይ" else "View All"

    // Telemetry
    fun inspectionTelemetry(isAm: Boolean) = if (isAm) "የፍተሻ ስታትስቲክስ" else "Inspection Telemetry"
    fun totalScans(isAm: Boolean) = if (isAm) "ጠቅላላ ፍተሻ" else "Scans Total"
    fun genuine(isAm: Boolean) = if (isAm) "ትክክለኛ" else "Genuine"
    fun forged(isAm: Boolean) = if (isAm) "የተጭበረበረ" else "Forged"
    fun intercepted(isAm: Boolean) = if (isAm) "የተያዙ" else "Intercepted"
    fun thisMonth(isAm: Boolean) = if (isAm) "በዚህ ወር" else "This month"

    // Alert Banner
    fun criticalAlert(isAm: Boolean) = if (isAm) "አስቸኳይ ማስጠንቀቂያ • መርካቶ ቀጠና" else "CRITICAL ALERT • Merkato Zone"
    fun alertDesc(isAm: Boolean) = if (isAm) "የተጭበረበረ 200 ብር በስርጭት ላይ ይገኛል" else "Counterfeit 200 ETB In Circulation"
    fun alertDetail(isAm: Boolean) = if (isAm) "3 ተመሳሳይ የተጭበረበሩ ተከታታይ ቁጥሮች በመርካቶ አካባቢ ተገኝተዋል" else "3 forged serial series flagged near Merkato, Addis Ababa within the last hour."
    fun reviewSignatures(isAm: Boolean) = if (isAm) "ምልክቶችን ይመርምሩ" else "Review Signatures"

    // Inspection
    fun genuineBanknote(isAm: Boolean) = if (isAm) "ትክክለኛ የኢትዮጵያ ብር" else "GENUINE BANKNOTE"
    fun counterfeitDetected(isAm: Boolean) = if (isAm) "የተጭበረበረ ብር ተገኝቷል!" else "COUNTERFEIT DETECTED"
    fun certifiedHash(isAm: Boolean) = if (isAm) "የኢትዮጵያ ብሔራዊ ባንክ (NBE) የተረጋገጠ" else "National Bank of Ethiopia (NBE) Certified Vault Hash"
    fun highAuthenticity(isAm: Boolean) = if (isAm) "ከፍተኛ ትክክለኛነት ምዘና" else "High Authenticity Rating"
    fun counterfeitRisk(isAm: Boolean) = if (isAm) "የማጭበርበር ስጋት" else "Counterfeit Risk"
    fun nbeStatus(isAm: Boolean) = if (isAm) "የNBE መዝገብ ሁኔታ" else "NBE Registry Status"
    fun scanNext(isAm: Boolean) = if (isAm) "ቀጣዩን ብር ይመርምሩ" else "Scan Next Banknote"
    fun callPolice(isAm: Boolean) = if (isAm) "ለፌዴራል ፖሊስ ይደውሉ (991 / 8044)" else "Call Federal Police Hotline (991 / 8044)"
    fun broadcastHeatmap(isAm: Boolean) = if (isAm) "ለቀጥታ ካርታው ሪፖርት አድርግ" else "Broadcast to Live Fraud Heatmap"
    fun legalDirective(isAm: Boolean) = if (isAm) "ማስጠንቀቂያ • የህግ መመሪያ" else "Legal Safekeeping Directive"
    fun legalDesc(isAm: Boolean) = if (isAm) "በኢትዮጵያ ፌዴራል አዋጅ (አዋጅ ቁጥር 1110/2019) መሰረት የተጭበረበረ ገንዘብ ማሰራጨት ወይም መልሶ መስጠት በጥብቅ የተከለከለ ነው።" else "Under Ethiopian Federal Law (Proc. 1110/2019), it is strictly unlawful to circulate, pass on, or return counterfeit currency to the payer."
}
