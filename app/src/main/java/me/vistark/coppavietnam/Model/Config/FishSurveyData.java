package me.vistark.coppavietnam.Model.Config;

import android.graphics.Bitmap;

import me.vistark.coppavietnam.MainActivity;
import me.vistark.coppavietnam.Model.Entity.fsElement;

import me.vistark.coppavietnam.R;
import me.vistark.coppavietnam.TNLib;

import java.util.ArrayList;

public class FishSurveyData {
    public final static String TAG = FishSurveyData.class.getSimpleName();
    MainActivity mContext;
    ArrayList<fsElement> elements;

    public FishSurveyData(MainActivity mContext) {
        this.mContext = mContext;
        elements = new ArrayList<>();
    }

    public ArrayList<fsElement> CreateData() {
        elements.add(new fsElement(1, 1, "Thunnus albacares - Cá ngừ vây vàng", BitmapFromID(R.drawable.feature_image_1), ""));
        elements.add(new fsElement(2, 1, "Thunnus obesus - Cá ngừ mắt to", BitmapFromID(R.drawable.feature_image_2), ""));
        elements.add(new fsElement(3, 1, "Thunnus alalunga - Cá ngừ vây dài", BitmapFromID(R.drawable.feature_image_3), ""));
        elements.add(new fsElement(4, 1, "Thunnus orientalis - Cá ngừ vây xanh", BitmapFromID(R.drawable.feature_image_4), ""));
        elements.add(new fsElement(5, 1, "Thunnus tonggol - Cá ngừ bò", BitmapFromID(R.drawable.feature_image_5), ""));
        elements.add(new fsElement(6, 1, "Katsuwonus pelamis - Cá ngừ vằn", BitmapFromID(R.drawable.feature_image_6), ""));
        elements.add(new fsElement(7, 2, "Gempylus serpens - Cá thu rắn", BitmapFromID(R.drawable.feature_image_7), ""));
        elements.add(new fsElement(8, 2, "Acanthocybium solandri - Cá thu ngàng (cá thu hũ)", BitmapFromID(R.drawable.feature_image_8), ""));
        elements.add(new fsElement(9, 2, "Scomberomoros commerson - Cá thu", BitmapFromID(R.drawable.feature_image_9), ""));
        elements.add(new fsElement(10, 2, "Rainbow runner - Cá nục thu", BitmapFromID(R.drawable.feature_image_10), ""));
        elements.add(new fsElement(11, 2, "Yellowtail amberjack (Seriola dumrili) - Cá cam", BitmapFromID(R.drawable.feature_image_11), ""));
        elements.add(new fsElement(12, 2, "Lepidocybium flavobrunneum - Cá thầy bói (Cá đen)", BitmapFromID(R.drawable.feature_image_12), ""));
        elements.add(new fsElement(13, 2, "Coryphaena hippurus - Cá dũa (cá bè dũa, cá nục heo)", BitmapFromID(R.drawable.feature_image_13), ""));
        elements.add(new fsElement(14, 2, "Cottonmouth jack (Uraspsis helvola) - Cá khế (cá áo 6 sọc)", BitmapFromID(R.drawable.feature_image_14), ""));
        elements.add(new fsElement(15, 2, "Sickle pomfret - Sickle pomfret", BitmapFromID(R.drawable.feature_image_15), ""));
        elements.add(new fsElement(16, 3, "Makaira nigricans - Cá Maclin xanh", BitmapFromID(R.drawable.feature_image_16), ""));
        elements.add(new fsElement(17, 3, "Makaira indica - Cá Maclin đen", BitmapFromID(R.drawable.feature_image_17), ""));
        elements.add(new fsElement(18, 3, "Alepisaurus ferox - Cá hố ma", BitmapFromID(R.drawable.feature_image_18), ""));
        elements.add(new fsElement(19, 3, "Sphyraena barracuda - Cá nhồng lớn", BitmapFromID(R.drawable.feature_image_19), ""));
        elements.add(new fsElement(20, 3, "Istiophorus platypterus - Cá cờ lá (Cá cờ buồm)", BitmapFromID(R.drawable.feature_image_20), ""));
        elements.add(new fsElement(21, 3, "Xiphias gladius - Cá cờ kiếm (Cá kiếm)", BitmapFromID(R.drawable.feature_image_21), ""));
        elements.add(new fsElement(22, 3, "Oilfish (Ruvettus pretiosus) - Cá dầu", BitmapFromID(R.drawable.feature_image_22), ""));
        elements.add(new fsElement(23, 3, "Opah (Lampris guttatus) - Cá đỏ (mặt trăng, mặt trời)", BitmapFromID(R.drawable.feature_image_23), ""));
        elements.add(new fsElement(24, 4, "Pelagic pupfer (Tetraodon lagocephalus) - Cá nóc", BitmapFromID(R.drawable.feature_image_24), ""));
        elements.add(new fsElement(25, 4, "Pelagic Porcupinefish - Cá nóc nhím", BitmapFromID(R.drawable.feature_image_25), ""));
        elements.add(new fsElement(26, 4, "Black swallower (Chiasmodon niger) - Cá miệng rắn", BitmapFromID(R.drawable.feature_image_26), ""));
        elements.add(new fsElement(27, 4, "Hammerjaw (Omosudis lowei) - Cá vây tia", BitmapFromID(R.drawable.feature_image_27), ""));
        elements.add(new fsElement(28, 4, "Hairtail (Trichiurus sp./Lepidotus sp.) - Cá hố", BitmapFromID(R.drawable.feature_image_28), ""));
        elements.add(new fsElement(29, 4, "Driftfish - Cá chim 2 vây", BitmapFromID(R.drawable.feature_image_29), ""));
        elements.add(new fsElement(30, 4, "Pelagic trigger (Camthidermis maculates) - Cá bò", BitmapFromID(R.drawable.feature_image_30), ""));
        elements.add(new fsElement(31, 5, "Alopias superciliosus - Cá nhám chó mắt to", BitmapFromID(R.drawable.feature_image_31), ""));
        elements.add(new fsElement(32, 5, "Galeocerdo cuvier - Cá mập hổ", BitmapFromID(R.drawable.feature_image_32), ""));
        elements.add(new fsElement(33, 5, "Blacktip (Carcharhinus limbatus) - Cá mập chỏm vây đen", BitmapFromID(R.drawable.feature_image_33), ""));
        elements.add(new fsElement(34, 5, "Prionace glauca - Cá mập xanh", BitmapFromID(R.drawable.feature_image_34), ""));
        elements.add(new fsElement(35, 5, "Sphyrna lewini - Cá nhám cào (Nhám búa)", BitmapFromID(R.drawable.feature_image_35), ""));
        elements.add(new fsElement(36, 5, "Galapagos shark (Carcharhinus galapagenis) - Cá mập Galapagô", BitmapFromID(R.drawable.feature_image_36), ""));
        elements.add(new fsElement(37, 5, "Marko shark - Cá mập Marko", BitmapFromID(R.drawable.feature_image_37), ""));
        elements.add(new fsElement(38, 5, "Oceanic white-tip shark (Carcharhinus longimanus) - Cá mập vây ngực dài", BitmapFromID(R.drawable.feature_image_38), ""));
        elements.add(new fsElement(39, 5, "Sandbar shark (Carcharhinus plumbeus) - Cá mập bò (Cá mập nâu)", BitmapFromID(R.drawable.feature_image_39), ""));
        elements.add(new fsElement(40, 5, "Silky shark (Carcharhinus falciformis) - Cá mập mượt (Cá mập mồm rộng)", BitmapFromID(R.drawable.feature_image_40), ""));
        elements.add(new fsElement(41, 5, "Alopias pelagicus - Cá nhám đuôi dài", BitmapFromID(R.drawable.feature_image_41), ""));
        elements.add(new fsElement(42, 5, "Pseudocarcharias kamoharai - Cá mập sấu", BitmapFromID(R.drawable.feature_image_42), ""));
        elements.add(new fsElement(43, 6, "Mobula spp. - Cá đuối dơi", BitmapFromID(R.drawable.feature_image_43), ""));
        elements.add(new fsElement(44, 6, "Banded Eagle ray (Aetomylaeus nichofii) - Cá ó không gai", BitmapFromID(R.drawable.feature_image_44), ""));
        elements.add(new fsElement(45, 6, "Blotched Fantail ray (Taeniura meyeni) - Cá đuối", BitmapFromID(R.drawable.feature_image_45), ""));
        elements.add(new fsElement(46, 6, "Zebra shark (Stegostoma fasciatum) - Cá nhám nhu mì", BitmapFromID(R.drawable.feature_image_46), ""));
        elements.add(new fsElement(47, 7, "Green turtle - Rùa xanh", BitmapFromID(R.drawable.feature_image_47), ""));
        elements.add(new fsElement(48, 7, "Hawksbill turtle - Rùa Hawksbill", BitmapFromID(R.drawable.feature_image_48), ""));
        elements.add(new fsElement(49, 7, "Leatherback turtle - Rùa da lưng", BitmapFromID(R.drawable.feature_image_49), ""));
        elements.add(new fsElement(50, 7, "Loggerhead turtle - Rùa đầu to", BitmapFromID(R.drawable.feature_image_50), ""));
        elements.add(new fsElement(51, 7, "Olive Ridley turtle - Rùa Olive Ridley", BitmapFromID(R.drawable.feature_image_51), ""));
        return elements;
    }



    Bitmap BitmapFromID(int id) {
        return TNLib.Using.DrawableToBitmap(mContext, id);
    }
}
