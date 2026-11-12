# CounterWatch: Sample Test Data & Persistent Scanner Navigation

Integrate official sample banknote images (`genuine_200`, `counterfeit_200`, `genuine_100`, `counterfeit_100`) into the scanner interface for live demonstration and testing, incorporate the persistent bottom navigation bar into the scanner UI, and execute offline TensorFlow Lite inference displaying the exact class label and confidence score.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following revisions have been incorporated:

- **Sample Banknote Test Tray in Scanner**:
  - Sample images from `sample_data/` (`genuine_200_front_0_1190.jpg`, `counterfeit_200_front_0_1512.jpg`, `genuine_100_front_0_1741.jpg`, `counterfeit_100_front_0_237.jpg`) are packaged into the application assets and drawable resources.
  - A dedicated **"Test Specimen (Demonstration)"** quick tray is embedded directly above the camera controls, allowing users to test genuine vs. counterfeit notes instantly without needing physical Ethiopian paper banknotes in hand.
- **Persistent Bottom Navigation Bar in Scanner**:
  - `ScannerScreen` now embeds `BottomShieldNavigation` in its `Scaffold(bottomBar = ...)`, allowing seamless 1-tap switching between **Home**, **Scan**, **Fraud Map**, **Alerts**, and **Intelligence** without having to press the back button.
- **Photo Capture & TFLite Model Execution**:
  - Camera capture (`ImageCapture.takePicture`) and Gallery picker (`PickVisualMedia`) convert images to Bitmap, resize to 224x224 float32, and run through `Dense121_best_weights_mixed-III.tflite`.
  - Predictions display the model's exact 4 class labels:
    - `genuine_200_etb`
    - `counterfeit_200_etb`
    - `genuine_100_etb`
    - `counterfeit_100_etb`
  - Exact confidence percentage (e.g. `98.4%`) is displayed in an instant HUD banner and passed to the Inspection Analysis Report.

---

## 1. Scanner UI Layout with Bottom Bar & Sample Data Tray

```
┌────────────────────────────────────────────────────────────────────────┐
│                        SCANNER SCREEN HUD                              │
├────────────────────────────────────────────────────────────────────────┤
│  [<-] Scan Note                         [AI ACTIVE 60 FPS]     [Torch] │
│  Chips: [ETB 200]  [ETB 100]  [ETB 50]  [Batch Scan]                   │
├────────────────────────────────────────────────────────────────────────┤
│                                                                        │
│                      CAMERA VIEWPORT / RETICLE                         │
│                                                                        │
├────────────────────────────────────────────────────────────────────────┤
│  🧪 DEMO TEST SPECIMENS (From GitHub Sample Data):                    │
│  [200 Genuine]  [200 Counterfeit]  [100 Genuine]  [100 Counterfeit]    │
├────────────────────────────────────────────────────────────────────────┤
│  [🖼️ Upload]            [ 📷 SNAP & SCAN ]            [ ☀️ UV Light ]  │
│  Diagnostics: Watermark Match 99%  •  Micro-lettering Resolving        │
├────────────────────────────────────────────────────────────────────────┤
│                     BOTTOM SHIELD NAVIGATION BAR                       │
│     🏠 Home       📷 Scan (Active)     🗺️ Map     🔔 Alerts    👤 Intel│
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Technical Implementation Steps

1. **Ingest Sample Banknotes**:
   - Copy key samples from `/tmp/etb_repo/sample_data/` into `app/src/main/res/drawable/`:
     - `sample_genuine_200.jpg`
     - `sample_counterfeit_200.jpg`
     - `sample_genuine_100.jpg`
     - `sample_counterfeit_100.jpg`
2. **Update `BanknoteClassifier.kt`**:
   - Ensure the 4 classes `['genuine_200_etb', 'counterfeit_200_etb', 'genuine_100_etb', 'counterfeit_100_etb']` return:
     - `rawLabel`: Exact string from model
     - `confidence`: Formatted float percentage
     - `isGenuine`: Boolean flag (`true` for `genuine_*`, `false` for `counterfeit_*`)
     - `denomination`: `100` or `200`
3. **Update `ScannerScreen.kt`**:
   - Add `BottomShieldNavigation` to `Scaffold(bottomBar = ...)` with `onNavigate` callbacks.
   - Add a horizontal **"Sample Banknotes for Testing"** card row displaying real sample specimens with one-tap inference.
   - Implement `imageCapture.takePicture()` callback to classify live camera snapshots.
   - Implement `rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia())` for gallery uploads.
   - Render an on-screen preview pill displaying the detected `rawLabel` (e.g., `genuine_200_etb`) and confidence before showing the inspection report.
4. **Compile & Verify**:
   - Verify the build with `compile_applet`.
