package com.blogspot.tndev1403.fishSurvey.data.config;

import android.graphics.Bitmap;

import com.blogspot.tndev1403.fishSurvey.view.splashscreen.SplashScreenActivity;
import com.blogspot.tndev1403.fishSurvey.model.Fish;
import com.blogspot.tndev1403.fishSurvey.utils.ProcessingLibrary;
import com.blogspot.tndev1403.fishSurvey.R;

import java.util.ArrayList;

/*
 * Project: COPPA
 * Author: Nghia Nguyen
 * Email: projects.futuresky@gmail.com
 * Description: Contains all default of application
 */

public class DefaultFishData {
    private SplashScreenActivity mContext;
    private ArrayList<Fish> elements;

    public DefaultFishData(SplashScreenActivity mContext) {
        this.mContext = mContext;
        elements = new ArrayList<>();
    }

    // Generate data function
    public ArrayList<Fish> generate() {
        elements.add(new Fish(1, 1, "Thunnus albacares - Cá ngừ vây vàng", BitmapFromID(R.drawable.feature_image_1), ""));
        elements.add(new Fish(2, 1, "Thunnus obesus - Cá ngừ mắt to", BitmapFromID(R.drawable.feature_image_2), ""));
        elements.add(new Fish(3, 1, "Thunnus alalunga - Cá ngừ vây dài", BitmapFromID(R.drawable.feature_image_3), ""));
        elements.add(new Fish(4, 1, "Thunnus orientalis - Cá ngừ vây xanh", BitmapFromID(R.drawable.feature_image_4), ""));
        elements.add(new Fish(5, 1, "Thunnus tonggol - Cá ngừ bò", BitmapFromID(R.drawable.feature_image_5), ""));
        elements.add(new Fish(6, 1, "Katsuwonus pelamis - Cá ngừ vằn", BitmapFromID(R.drawable.feature_image_6), ""));
        elements.add(new Fish(7, 2, "Gempylus serpens - Cá thu rắn", BitmapFromID(R.drawable.feature_image_7), ""));
        elements.add(new Fish(8, 2, "Acanthocybium solandri - Cá thu ngàng (cá thu hũ)", BitmapFromID(R.drawable.feature_image_8), ""));
        elements.add(new Fish(9, 2, "Scomberomoros commerson - Cá thu", BitmapFromID(R.drawable.feature_image_9), ""));
        elements.add(new Fish(10, 2, "Rainbow runner - Cá nục thu", BitmapFromID(R.drawable.feature_image_10), ""));
        elements.add(new Fish(11, 2, "Yellowtail amberjack (Seriola dumrili) - Cá cam", BitmapFromID(R.drawable.feature_image_11), ""));
        elements.add(new Fish(12, 2, "Lepidocybium flavobrunneum - Cá thầy bói (Cá đen)", BitmapFromID(R.drawable.feature_image_12), ""));
        elements.add(new Fish(13, 2, "Coryphaena hippurus - Cá dũa (cá bè dũa, cá nục heo)", BitmapFromID(R.drawable.feature_image_13), ""));
        elements.add(new Fish(14, 2, "Cottonmouth jack (Uraspsis helvola) - Cá khế (cá áo 6 sọc)", BitmapFromID(R.drawable.feature_image_14), ""));
        elements.add(new Fish(15, 2, "Sickle pomfret - Sickle pomfret", BitmapFromID(R.drawable.feature_image_15), ""));
        elements.add(new Fish(16, 3, "Makaira nigricans - Cá Maclin xanh", BitmapFromID(R.drawable.feature_image_16), ""));
        elements.add(new Fish(17, 3, "Makaira indica - Cá Maclin đen", BitmapFromID(R.drawable.feature_image_17), ""));
        elements.add(new Fish(18, 3, "Alepisaurus ferox - Cá hố ma", BitmapFromID(R.drawable.feature_image_18), ""));
        elements.add(new Fish(19, 3, "Sphyraena barracuda - Cá nhồng lớn", BitmapFromID(R.drawable.feature_image_19), ""));
        elements.add(new Fish(20, 3, "Istiophorus platypterus - Cá cờ lá (Cá cờ buồm)", BitmapFromID(R.drawable.feature_image_20), ""));
        elements.add(new Fish(21, 3, "Xiphias gladius - Cá cờ kiếm (Cá kiếm)", BitmapFromID(R.drawable.feature_image_21), ""));
        elements.add(new Fish(22, 3, "Oilfish (Ruvettus pretiosus) - Cá dầu", BitmapFromID(R.drawable.feature_image_22), ""));
        elements.add(new Fish(23, 3, "Opah (Lampris guttatus) - Cá đỏ (mặt trăng, mặt trời)", BitmapFromID(R.drawable.feature_image_23), ""));
        elements.add(new Fish(24, 4, "Pelagic pupfer (Tetraodon lagocephalus) - Cá nóc", BitmapFromID(R.drawable.feature_image_24), ""));
        elements.add(new Fish(25, 4, "Pelagic Porcupinefish - Cá nóc nhím", BitmapFromID(R.drawable.feature_image_25), ""));
        elements.add(new Fish(26, 4, "Black swallower (Chiasmodon niger) - Cá miệng rắn", BitmapFromID(R.drawable.feature_image_26), ""));
        elements.add(new Fish(27, 4, "Hammerjaw (Omosudis lowei) - Cá vây tia", BitmapFromID(R.drawable.feature_image_27), ""));
        elements.add(new Fish(28, 4, "Hairtail (Trichiurus sp./Lepidotus sp.) - Cá hố", BitmapFromID(R.drawable.feature_image_28), ""));
        elements.add(new Fish(29, 4, "Driftfish - Cá chim 2 vây", BitmapFromID(R.drawable.feature_image_29), ""));
        elements.add(new Fish(30, 4, "Pelagic trigger (Camthidermis maculates) - Cá bò", BitmapFromID(R.drawable.feature_image_30), ""));
        elements.add(new Fish(31, 5, "Alopias superciliosus - Cá nhám chó mắt to", BitmapFromID(R.drawable.feature_image_31), ""));
        elements.add(new Fish(32, 5, "Galeocerdo cuvier - Cá mập hổ", BitmapFromID(R.drawable.feature_image_32), ""));
        elements.add(new Fish(33, 5, "Blacktip (Carcharhinus limbatus) - Cá mập chỏm vây đen", BitmapFromID(R.drawable.feature_image_33), ""));
        elements.add(new Fish(34, 5, "Prionace glauca - Cá mập xanh", BitmapFromID(R.drawable.feature_image_34), ""));
        elements.add(new Fish(35, 5, "Sphyrna lewini - Cá nhám cào (Nhám búa)", BitmapFromID(R.drawable.feature_image_35), ""));
        elements.add(new Fish(36, 5, "Galapagos shark (Carcharhinus galapagenis) - Cá mập Galapagô", BitmapFromID(R.drawable.feature_image_36), ""));
        elements.add(new Fish(37, 5, "Marko shark - Cá mập Marko", BitmapFromID(R.drawable.feature_image_37), ""));
        elements.add(new Fish(38, 5, "Oceanic white-tip shark (Carcharhinus longimanus) - Cá mập vây ngực dài", BitmapFromID(R.drawable.feature_image_38), ""));
        elements.add(new Fish(39, 5, "Sandbar shark (Carcharhinus plumbeus) - Cá mập bò (Cá mập nâu)", BitmapFromID(R.drawable.feature_image_39), ""));
        elements.add(new Fish(40, 5, "Silky shark (Carcharhinus falciformis) - Cá mập mượt (Cá mập mồm rộng)", BitmapFromID(R.drawable.feature_image_40), ""));
        elements.add(new Fish(41, 5, "Alopias pelagicus - Cá nhám đuôi dài", BitmapFromID(R.drawable.feature_image_41), ""));
        elements.add(new Fish(42, 5, "Pseudocarcharias kamoharai - Cá mập sấu", BitmapFromID(R.drawable.feature_image_42), ""));
        elements.add(new Fish(43, 6, "Mobula spp. - Cá đuối dơi", BitmapFromID(R.drawable.feature_image_43), ""));
        elements.add(new Fish(44, 6, "Banded Eagle ray (Aetomylaeus nichofii) - Cá ó không gai", BitmapFromID(R.drawable.feature_image_44), ""));
        elements.add(new Fish(45, 6, "Blotched Fantail ray (Taeniura meyeni) - Cá đuối", BitmapFromID(R.drawable.feature_image_45), ""));
        elements.add(new Fish(46, 6, "Zebra shark (Stegostoma fasciatum) - Cá nhám nhu mì", BitmapFromID(R.drawable.feature_image_46), ""));
        elements.add(new Fish(47, 7, "Green turtle - Rùa xanh", BitmapFromID(R.drawable.feature_image_47), ""));
        elements.add(new Fish(48, 7, "Hawksbill turtle - Rùa Hawksbill", BitmapFromID(R.drawable.feature_image_48), ""));
        elements.add(new Fish(49, 7, "Leatherback turtle - Rùa da lưng", BitmapFromID(R.drawable.feature_image_49), ""));
        elements.add(new Fish(50, 7, "Loggerhead turtle - Rùa đầu to", BitmapFromID(R.drawable.feature_image_50), ""));
        elements.add(new Fish(51, 7, "Olive Ridley turtle - Rùa Olive Ridley", BitmapFromID(R.drawable.feature_image_51), ""));
        return elements;
    }

    private Bitmap BitmapFromID(int id) {
        return ProcessingLibrary.Using.DrawableToBitmap(mContext, id);
    }
}
