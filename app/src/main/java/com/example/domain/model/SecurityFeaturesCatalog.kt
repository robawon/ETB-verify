package com.example.domain.model

object SecurityFeaturesCatalog {
    val features = listOf(
        SecurityFeatureItem(
            id = 1,
            number = "1",
            titleAm = "ተከታታይ ቁጥሮች",
            titleEn = "Serial Numbers",
            badge = "Serial Numbers",
            descAm = "የገንዘቡ ቁጥሮች በማይክሮ ቴክኖሎጂ የታተሙ ሲሆን በአልትራቫዮሌት (UV) ብርሃን ስር ሲታዩ ወደ ደማቅ አረንጓዴ ቀለም ይቀየራሉ።",
            descEn = "Under UV/fluorescent counterfeit verification devices, the black serial numbers illuminate into vivid phosphorescent emerald green.",
            actionTag = "UV Fluorescent Light Test"
        ),
        SecurityFeatureItem(
            id = 2,
            number = "2",
            titleAm = "ማየት ለተሳናቸው እውቅና ምልክት",
            titleEn = "Tactile Marks",
            badge = "Tactile Marks",
            descAm = "የብር ኖቱን ጠርዝ በመዳሰስ ማየት የተሳናቸው ሰዎች በቀላሉ የኖቱን ዋጋ እንዲያውቁ የሚያስችሉ ጎበጥ ያሉ መስመሮች አሉት።",
            descEn = "Heavily raised intaglio tactile ink ridges engraved along edges allowing instant fingertip identification for the visually impaired.",
            actionTag = "Tactile Edge Feel"
        ),
        SecurityFeatureItem(
            id = 3,
            number = "3",
            titleAm = "የቅርብ መስመሮች (PEAK®)",
            titleEn = "PEAK® Intaglio",
            badge = "PEAK® Intaglio",
            descAm = "የገንዘብ ኖቱ ሲያጋድል የመልኩን አቅጣጫ በመቀየር የብር መጠኑ በግልጽ ጎልቶ ይታያል።",
            descEn = "Latent high-relief intaglio line patterns revealing the note's face numerical value clearly when viewed at a glancing shallow angle.",
            actionTag = "Tilt & Glancing Angle"
        ),
        SecurityFeatureItem(
            id = 4,
            number = "4",
            titleAm = "ደማቅ አንጸባራቂ ምልክት (SPARK®)",
            titleEn = "SPARK® Dynamic",
            badge = "SPARK® Dynamic",
            descAm = "ገንዘቡን ወደ ላይ ወይም ወደ ታች ሲያዘነብሉት የሚያንጸባርቅ የቀለም ቅስት (ቀስተ ደመና) አቅጣጫውን ይቀይራል።",
            descEn = "Optically variable dynamic ink rolling in an arc across the motif with smooth, brilliant color transformation when tilted.",
            actionTag = "Optical Color Motion"
        ),
        SecurityFeatureItem(
            id = 5,
            number = "5",
            titleAm = "የደህንነት መጠበቂያ ክር",
            titleEn = "Security Thread",
            badge = "Security Thread",
            descAm = "የሚንቀሳቀስ ደማቅ ቀለም ያለው ከተከታታይ የብርሃን ነጸብራቆች ጋር 'NBE' የሚል ጽሁፍ የያዘ ክር በብር ኖቱ መሃል ላይ ገብቷል።",
            descEn = "Woven segmented windowed metallic security ribbon showing micro-lettering 'NBE' and denomination numeral with kinetic color shifts.",
            actionTag = "Integrated Metallic Ribbon"
        ),
        SecurityFeatureItem(
            id = 6,
            number = "6",
            titleAm = "የውሃ ምልክት",
            titleEn = "Watermark",
            badge = "Watermark",
            descAm = "ገንዘቡን ወደ ብርሃን አቅጣጫ በማንሳት ሲመለከቱት በወረቀቱ ውስጥ የተቀረጸው አንበሳ ወይም እርግብ ምስል በግልጽ ይታያል።",
            descEn = "Multi-tone watermark embedded inside the high-grade cotton pulp, revealing realistic soft gradients and denomination figures when backlit.",
            actionTag = "Backlit Light Check"
        ),
        SecurityFeatureItem(
            id = 7,
            number = "7",
            titleAm = "ትይዩ ምልክት",
            titleEn = "See-Through Register",
            badge = "See-Through Register",
            descAm = "የገንዘቡ የፊትና የኋላ ገጽ ምልክቶች በብርሃን ሲታዩ ፍጹም ተገጣጥመው አንድ ወጥ ክብ ምልክት ይፈጥራሉ።",
            descEn = "Precision micro-aligned front and reverse printing that fits with mathematical perfection to form a unified circular emblem against direct light.",
            actionTag = "Front-to-Back Match"
        ),
        SecurityFeatureItem(
            id = 8,
            number = "8",
            titleAm = "ፍሎረሰንስ ምልክት",
            titleEn = "Fluorescent Patch",
            badge = "Fluorescent & Patch",
            descAm = "በአልትራቫዮሌት ጨረር ስር ሲመረመር በደማቅ ቀለም የሚያበሩ ልዩ ቅንጣቶችና የጸጥታ አርማዎች በገጹ ላይ ይገኛሉ።",
            descEn = "Fluorescent security ink particles and specialized emblem patches that light up intensely under ultraviolet frequency bands.",
            actionTag = "UV Glow Inspection"
        )
    )

    val spotChecks = listOf(
        SpotCheckItem(
            id = 1,
            titleEn = "1. Substrate & Texture",
            titleAm = "1. የወረቀት ስሜት (Substrate & Texture)",
            genuineEn = "Crisp cotton-linen; crackling texture when flexed",
            genuineAm = "የጥጥ ጥራት ያለው ወረቀት፤ ሲታጠፍ ድምፅ የሚያሰማ",
            counterfeitEn = "Slick photocopy paper; limp and glossy surface",
            counterfeitAm = "ተራ የኮፒ ወረቀት፤ ድምፅ የሌለውና ለስላሳ"
        ),
        SpotCheckItem(
            id = 2,
            titleEn = "2. Watermark Precision",
            titleAm = "2. የውሃ ምልክት (Watermark Precision)",
            genuineEn = "Intaglio depth variations visible under light",
            genuineAm = "በብርሃን ውስጥ ጥልቅ ጥላዎችና ግልጽ ምስል",
            counterfeitEn = "Flat offset printed surface ink; heavy and blurred",
            counterfeitAm = "ላይ ላይ የታተመ ቀለም፤ የደበዘዘ ምስል"
        ),
        SpotCheckItem(
            id = 3,
            titleEn = "3. Windowed Security Thread",
            titleAm = "3. የደህንነት ክር (Windowed Security Thread)",
            genuineEn = "Woven inside pulp; microtext 'NBE' razor-sharp",
            genuineAm = "በወረቀቱ ውስጥ የተሰፋ፤ የ'NBE' ጽሁፍ ግልጽ",
            counterfeitEn = "Glued foil tape or drawn metallic pen stripes",
            counterfeitAm = "የተለጠፈ ፕላስቲክ ወይም የተሰመረ ቀለም"
        )
    )
}
