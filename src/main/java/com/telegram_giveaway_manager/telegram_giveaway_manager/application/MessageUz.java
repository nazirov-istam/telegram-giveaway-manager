package com.telegram_giveaway_manager.telegram_giveaway_manager.application;

import org.springframework.stereotype.Component;

@Component
public final class MessageUz {
    public static final String channelText = "Iltimos, quyidagi kanallarga obuna bo'ling:00001 ➕ Tekshirish ➕ ";
    public static final String successText = """
            \uD83C\uDF89 Tabriklaymiz! Siz muvaffaqiyatli ravishda giveawayga qo'shildingiz! \s
            Omadingizni sinab ko'ring va g'oliblar orasida bo'lish imkoniyatini boy bermang. \uD83D\uDE0A \s
            G'oliblar e'lon qilinadigan sana: [sana]. \s
            O'zingizni do'stlaringiz bilan ulashing va ularga ham g'olib bo'lish imkoniyatini bering! \s

            Bizni kuzatishda davom eting! ✅
            """;

    public static final String entryMessage = """
            🎉 Assalomu alaykum! Giveaway botimizga xush kelibsiz! \s
            Bu yerda siz qiziqarli sovrinlar yutib olish imkoniyatiga ega bo'lasiz. 🎁 \s

            📋 Giveawayga qo'shilish juda oson: \s
            1️⃣ Qoidalarga amal qiling. \s
            2️⃣ G'oliblar qatorida bo'lish imkoniyatini qo'ldan boy bermang! \s

            Omadingizni sinab ko'ring va bizning maxsus sovrinlarimizni qo'lga kiriting!😊
            """;

    public static final String help = """
            Agar sizda takliflar/shikoyatlar/savollar bo'lsa pastdagi telefon raqam bilan bog'laning.
                       
            ☎️+(998)-99-663-66-65
            Telegram: @nazirov_istam""";
}
