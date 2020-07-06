package vn.softdreams.ebweb.service.Utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Pageable;
import vn.softdreams.ebweb.domain.GenCode;
import vn.softdreams.ebweb.domain.SAOrderDetails;
import vn.softdreams.ebweb.service.dto.UserDTO;
import vn.softdreams.ebweb.service.util.Constants;
import vn.softdreams.ebweb.service.util.DateUtil;

import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import javax.xml.bind.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Utils {

    /**
     * add by Hautv
     *
     * @param lstOrderFixCodeChild
     * @param orderFixCodeParent
     * @return
     */
    public static String GetOrderFixCode(List<String> lstOrderFixCodeChild, String orderFixCodeParent) {
        String orderFixCode = null;
        List<Integer> temp = new ArrayList<Integer>();
        int rs;
        for (String t : lstOrderFixCodeChild) {
            temp.add(orderFixCodeParent == null || orderFixCodeParent == ""
                ? Integer.parseInt(t)
                : Integer.parseInt(t.replace(orderFixCodeParent + "-", "")));
        }
        if (temp.size() == 0) {
            rs = 1;
        } else {
            rs = Collections.max(temp);
        }
        if (orderFixCodeParent == null || orderFixCodeParent == "")
            orderFixCode = String.valueOf(rs);
        else {
            orderFixCode = String.format("%s-%s", orderFixCodeParent, String.valueOf(rs));
        }
        return orderFixCode;
    }

    private static String[] ChuSo = {" không", " một", " hai", " ba", " bốn", " năm", " sáu", " bảy", " tám", " chín"};
    private static String[] Tien = {"", " nghìn", " triệu", " tỷ", " nghìn tỷ", " triệu tỷ"};

    /**
     * Author Hautv
     *
     * @param amount
     * @param currency
     * @return
     */
    // region Hàm đọc số thành chữ
    public static String GetAmountInWords(BigDecimal amount, String currency, UserDTO userDTO) {
        if (!currency.equals("VND")) {
            long dol = amount.longValue();
            amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
            String ret = "";
            BigDecimal cent = amount.subtract(BigDecimal.valueOf(dol));
            cent = cent.multiply(BigDecimal.valueOf(100));
            cent = cent.setScale(0, BigDecimal.ROUND_HALF_UP);
            if (currency.equals("USD")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " đô la Mỹ", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("EUR")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " Euro", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("JPY")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " Yên", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("SGD")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " đô la Sing", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("GBP")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " bảng Anh", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("CNY")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " nhân dân tệ", /*" chẵn",*/ false, userDTO);
                }
            }
            if (currency.equals("ETB")) {
                if (dol > 0) {
                    ret = GetAmountInWords(dol, " Birr Ethiopia", /*" chẵn",*/ false, userDTO);
                }
            }
            if (cent.compareTo(BigDecimal.ZERO) > 0) {
                String centString = String.valueOf(cent);
                ArrayList<Integer> array_I = new ArrayList<>();
                for (Character i : centString.toCharArray()) {
                    array_I.add(Integer.parseInt(i.toString()));
                }
                Collections.reverse(array_I);
                int tem = centString.length() == 1 ? array_I.get(0) : array_I.get(1) * 10 + array_I.get(0);
                if (ret.length() > 0)
                    ret += " và";
                ret += ReadGroupOfThree(tem, false, userDTO);
                if (currency.equals("USD") || currency.equals("EUR") || currency.equals("SGD")) {
                    ret += " cents";
                }
                if (currency.equals("GBP")) {
                    ret += " pence";
                }
                if (currency.equals("CNY")) {
                    ret += " hào";
                }
                if (currency.equals("ETB")) {
                    ret += " birr";
                }
            }
            if (ret == "" || ret == null) {
                if (currency.equals("USD"))
                    ret = "Không đô la Mỹ";
                if (currency.equals("EUR"))
                    ret = "Không Euro";
                if (currency.equals("JPY"))
                    ret = "Không Yên";
                if (currency.equals("SGD"))
                    ret = "Không đô la Sing";
                if (currency.equals("GBP"))
                    ret = "Không bảng Anh";
                if (currency.equals("CNY"))
                    ret = "Không nhân dân tệ";
                if (currency.equals("ETB"))
                    ret = "Không Birr";
            }
            if (ret == "" || ret == null) {
                ret = "Chưa thêm vào đọc tiền";
            } else {
                ret = ret.substring(0, 1).toUpperCase() + ret.substring(1) + ".";
            }
            return ret;
        }
        Integer TCKHAC_DocTienBangChu = TCKHAC_DocTienBangChu(userDTO);
        if (TCKHAC_DocTienBangChu == 1 && currency.equals("VND")) {
            return GetAmountInWords(amount.setScale(0, BigDecimal.ROUND_HALF_UP).longValue(), " đồng chẵ̃n", true, userDTO);
        } else {
            return GetAmountInWords(amount.setScale(0, BigDecimal.ROUND_HALF_UP).longValue(), " đồng", true, userDTO);
        }
    }

    /**
     * @param number
     * @param digit
     * @return
     * @Author Hautv
     */
    //làm tròn số
    public static BigDecimal round(BigDecimal number, int digit) {
        if (number == null) {
            return null;
        }
        return number.setScale(digit, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal round(BigDecimal number) {
        return round(number, 0);
    }

    private static String GetAmountInWords(long soTien, String currency, /*String evenText,*/ boolean includeDot, UserDTO userDTO) {
        Integer TCKHAC_DocTienBangChu = TCKHAC_DocTienBangChu(userDTO);

        int lan, i;
        long so;
        String ketQua = "";
        int[] viTri = new int[6];
        if (soTien < 0) return "Số tiền âm!";
        if (soTien == 0) {
            if (TCKHAC_DocTienBangChu == 1) {
                return "Không đồng chẵn!";
            } else {
                return "Không đồng!";
            }
        }
        if (soTien > 0) {
            so = soTien;
        } else {
            so = -soTien;
        }
        //Kiểm tra số quá lớn
        if (soTien > Long.valueOf("8999999999999999")) {
            soTien = 0;
            return "";
        }
        viTri[5] = (int) (so / Long.valueOf("1000000000000000"));
        so = so - Long.valueOf(String.valueOf(viTri[5])) * Long.valueOf("1000000000000000");
        viTri[4] = (int) (so / Long.valueOf("1000000000000"));
        so = so - Long.valueOf(String.valueOf(viTri[4])) * Long.valueOf("1000000000000");
        viTri[3] = (int) (so / 1000000000);
        so = so - Long.valueOf(String.valueOf(viTri[3])) * 1000000000;
        viTri[2] = (int) (so / 1000000);
        viTri[1] = (int) ((so % 1000000) / 1000);
        viTri[0] = (int) (so % 1000);
        if (viTri[5] > 0) {
            lan = 5;
        } else if (viTri[4] > 0) {
            lan = 4;
        } else if (viTri[3] > 0) {
            lan = 3;
        } else if (viTri[2] > 0) {
            lan = 2;
        } else if (viTri[1] > 0) {
            lan = 1;
        } else {
            lan = 0;
        }
        for (i = lan; i >= 0; i--) {
            boolean isDoc = false;
            if (String.valueOf(viTri[i]).length() < 3 && i < lan) {
                String testing = String.valueOf(viTri[i]);
                isDoc = true;
                //ViTri[i] = AddZero(ViTri[i]);
            }
            String tmp = ReadGroupOfThree(viTri[i], isDoc, userDTO);
            isDoc = false;
            ketQua += tmp;
            if (viTri[i] != 0) ketQua += Tien[i];
            if ((i > 0) && (tmp != null & tmp != "")) ketQua += "";//&& (!String.IsNullOrEmpty(tmp))
        }
        if (ketQua.substring(ketQua.length() - 1, ketQua.length()) == ",")
            ketQua = ketQua.substring(0, ketQua.length() - 1);
        ketQua = ketQua.trim();
        //if (soTien.ToString()[soTien.ToString().length() - 1] == '0')
        //    currency = currency + evenText;
        return ketQua.substring(0, 1).toUpperCase() + ketQua.substring(1) + currency + (includeDot ? "." : "");
    }

    private static String AddZero(String str) {
        if (str.length() == 2)
            str = "0" + str;
        else if (str.length() == 1)
            str = "00" + str;
        return str;

    }

    // Hàm đọc số có 3 chữ số
    private static String ReadGroupOfThree(int baso, boolean isDoc0, UserDTO userDTO) {
        Integer DDSo_DocTienLe = Utils.DDSo_DocTienLe(userDTO);
        String ketQua = "";
        int tram = (int) (baso / 100);
        int chuc = (int) ((baso % 100) / 10);
        int donvi = baso % 10;
        if ((tram == 0) && (chuc == 0) && (donvi == 0)) return "";
        if (tram != 0 || isDoc0) {
            ketQua += ChuSo[tram] + " trăm";
            if ((chuc == 0) && (donvi != 0)) {
                if (DDSo_DocTienLe == 0) {
                    ketQua += " linh";
                } else {
                    ketQua += " lẻ";
                }
            }
        }
        if ((chuc != 0) && (chuc != 1)) {
            ketQua += ChuSo[chuc] + " mươi";
            if ((chuc == 0) && (donvi != 0))
                if (DDSo_DocTienLe == 0) {
                    ketQua = ketQua + " linh";
                } else {
                    ketQua = ketQua + " lẻ";
                }
        }
        if (chuc == 1) ketQua += " mười";
        switch (donvi) {
            case 1:
                if ((chuc != 0) && (chuc != 1)) {
                    ketQua += " mốt";
                } else {
                    ketQua += ChuSo[donvi];
                }
                break;
            case 5:
                if (chuc == 0) {
                    ketQua += ChuSo[donvi];
                } else {
                    ketQua += " lăm";
                }
                break;
            default:
                if (donvi != 0) {
                    ketQua += ChuSo[donvi];
                }
                break;
        }
        return ketQua;
    }

    //endregion
    public static void setParams(@NotNull Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable, @NotNull Number total) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static String codeVoucher(GenCode genCode) {
        return String.format("%1$s%2$s%3$s", genCode.getPrefix(),
            StringUtils.leftPad(String.valueOf(genCode.getCurrentValue() + 1),
                genCode.getLength(), '0'),
            genCode.getSuffix() == null ? "" : genCode.getSuffix());
    }

    /**
     * Author Hautv
     *
     * @param amount
     * @param typeAmount Loại tiền (Đơn giá, ngoại tệ, ...) lấy từ bảng SystemOption
     * @return
     */
    public static String formatTien(BigDecimal amount, String typeAmount, UserDTO userDTO) {
        if (userDTO.getEmptyIfNull() && (amount == null || amount.compareTo(BigDecimal.ZERO) == 0)) {
            return "";
        }
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        String result = "";
        String format = "";
        DecimalFormat df;

        String nganCachHangDonVi = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangDVi)).findAny().get().getData();
        String nganCachHangNghin = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_NCachHangNghin)).findAny().get().getData();
        int hienThiSoAm = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_SoAm)).findAny().get().getData());
        int so0SauDauPhay = Integer.parseInt(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(typeAmount)).findAny().get().getData());
        String so_0 = net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.leftPad("", so0SauDauPhay, '0');
        format = String.format("#,##0.%1$s", so_0);
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(nganCachHangDonVi.toCharArray()[0]);
        decimalFormatSymbols.setGroupingSeparator(nganCachHangNghin.toCharArray()[0]);
        df = new DecimalFormat(format, decimalFormatSymbols);
        result = df.format(amount.abs());
        if (so0SauDauPhay == 0) {
            if (result.substring(result.length() - 1).equals(",")) {
                result = result.substring(0, result.length() - 1);
            }
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            if (hienThiSoAm == 0) {
                result = "(" + result + ")";
            } else {
                result = "-" + result;
            }
        }
        return result;
    }

    /**
     * Author Hautv
     *
     * @param userDTO
     * @return
     */
    public static Integer PhienSoLamViec(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.PHIEN_SoLamViec)).findAny().get().getData());
    }

    public static Integer DDSo_DonGia(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_DonGia)).findAny().get().getData());
    }

    public static Integer DDSo_DocTienLe(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DDSo_DocTienLe)).findAny().get().getData());
    }

    public static Integer TCKHAC_DocTienBangChu(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_DocTienBangChu)).findAny().get().getData());
    }

    public static String DBDateClosed(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.DBDateClosed)).findAny().get().getData();
    }

    public static Integer TCKHAC_SDDMDoiTuong(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMDoiTuong)).findAny().get().getData());
    }

    public static String getEI_IDNhaCungCapDichVu(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.EI_IDNhaCungCapDichVu)).findAny().get().getData();
    }

    public static String getEI_TenDangNhap(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.EI_TenDangNhap)).findAny().get().getData();
    }

    public static Integer getTCKHAC_SDDMVTHH(UserDTO userDTO) {
        return Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDDMVTHH)).findAny().get().getData());
    }

    public static String getEI_MatKhau(UserDTO userDTO) {
        String result = null;
        String passAES = userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.EI_MatKhau)).findAny().get().getData();
        if (!StringUtils.isEmpty(passAES)) {
            result = decryptAES_CBC_Base64(passAES);
        }
        return result;
    }

    public static String getEI_MatKhauAES(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.EI_MatKhau)).findAny().get().getData();
    }

    public static String getEI_Path(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.EI_Path)).findAny().get().getData();
    }

    public static String getEI_Token_MIV(UserDTO userDTO) {
        return userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.Token_MIV)).findAny().get().getData();
    }

    /**
     * Author Hautv
     *
     * @param userDTO
     * @return
     */
    public static Boolean getTCKHAC_SDTichHopHDDT(UserDTO userDTO) {
        Integer data = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.TCKHAC_SDTichHopHDDT)).findAny().get().getData());
        if (data.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Author Hautv
     *
     * @param userDTO
     * @return
     */
    public static Boolean getCheckHDCD(UserDTO userDTO) {
        Integer data = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.CheckHDCD)).findAny().get().getData());
        if (data.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Author Hautv
     * false : ký server
     * true: ký token
     *
     * @param userDTO
     * @return
     */
    public static Boolean getSignType(UserDTO userDTO) {
        Integer data = Integer.valueOf(userDTO.getSystemOption().stream().filter(n -> n.getCode().equals(Constants.SystemOption.SignType)).findAny().get().getData());
        if (data.equals(1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param object
     * @return
     * @Author Hautv
     */
    public static String ObjectToXML(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        JAXB.marshal(object, sw);
        result = sw.toString();
        return result;
    }

    public static String jaxbObjectToXML(Object object) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

            StringWriter sw = new StringWriter();
            m.marshal(object, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    /**
     * @param xml
     * @param aClass
     * @return
     * @Author Hautv
     */
    public static Object xmlToObject(String xml, Class aClass) {
        Object object = null;
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(aClass);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            object = unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * @param json
     * @param aClass
     * @return
     * @Author Hautv
     */
    public static Object jsonToObject(String json, Class aClass) {
        Object object = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            object = mapper.readValue(json, aClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * @param object
     * @return
     * @Author Hautv
     */
    public static String ObjectToJSON(Object object) {
        String result = null;
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            result = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param object
     * @return
     * @Author Hautv
     */
    public static String ObjectToJSONIgnoreNull(Object object) {
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        try {
            result = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Author Hautv
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> readList(String str, Class<T> type) {
        return readList(str, ArrayList.class, type);
    }

    /**
     * @Author Hautv
     * @param str
     * @param type
     * @param elementType
     * @param <T>
     * @return
     */
    public static <T> List<T> readList(String str, Class<? extends Collection> type, Class<T> elementType) {
        final ObjectMapper mapper = newMapper();
        try {
            return mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(type, elementType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }

    /**
     * @param uuid
     * @return
     * @Author Hautv
     */
    public static UUID uuidConvertToGUID(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("Parameter, uuid cannot be null");
        }

        final long msbBigEndian = uuid.getMostSignificantBits();
        final long msbLittleEndian =
            ((msbBigEndian & 0x0000_00FF_0000_0000L) << 24) |
                ((msbBigEndian & 0x0000_FF00_0000_0000L) << 8) |
                ((msbBigEndian & 0x00FF_0000_0000_0000L) >>> 8) |
                ((msbBigEndian & 0xFF00_0000_0000_0000L) >>> 24) |
                ((msbBigEndian & 0x0000_0000_FF00_0000L) >>> 8) |
                ((msbBigEndian & 0x0000_0000_00FF_0000L) << 8) |
                ((msbBigEndian & 0x0000_0000_0000_FF00L) >>> 8) |
                ((msbBigEndian & 0x0000_0000_0000_00FFL) << 8);
        return new UUID(msbLittleEndian, uuid.getLeastSignificantBits());
    }

    /**
     * @param plainText
     * @param password
     * @return
     * @Author Hautv
     */
    public static String encryptAES_CBC_Base64(String plainText, String password) {
        ICrypt crypt = new AESCrypt();
        String encoded = null;
        try {
            encoded = crypt.encode(plainText, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    public static String encryptAES_CBC_Base64(String plainText) {
        ICrypt crypt = new AESCrypt();
        String encoded = null;
        try {
            encoded = crypt.encode(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }

    /**
     * @param securityText
     * @param password
     * @return
     * @Author Hautv
     */
    public static String decryptAES_CBC_Base64(String securityText, String password) {
        ICrypt crypt = new AESCrypt();
        String decode = null;
        if (!StringUtils.isEmpty(securityText)) {
            try {
                decode = crypt.decode(securityText, password);
            } catch (Exception e) {
                /*e.printStackTrace();*/
            }
        }
        return decode;
    }

    /**
     * @param securityText
     * @return
     * @Author Hautv
     */
    public static String decryptAES_CBC_Base64(String securityText) {
        ICrypt crypt = new AESCrypt();
        String decode = null;
        if (!StringUtils.isEmpty(securityText)) {
            try {
                decode = crypt.decode(securityText);
            } catch (Exception e) {
                /*e.printStackTrace();*/
            }
        }
        return decode;
    }

    /**
     * @param md5
     * @return
     * @Author Hautv
     */
    public static String MD5(String md5) {
        String result = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
           /* StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();*/
            result = Base64.getEncoder().encodeToString(array);
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param test
     * @return
     * @Author Hautv
     */
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (Exception ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (Exception ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param localDate
     * @param day
     * @return
     * @Author Hautv
     */
    public static LocalDate AddDate(LocalDate localDate, long day) {
        return localDate.plusDays(day).atTime(0, 0, 0, 0).toLocalDate();
    }

    /**
     * @return Sắp xêp Map</>
     * @Author Hautv
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * @param fromDate
     * @param toDate
     * @return
     * @Author Hautv
     */
    public static String getPeriod(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.getDayOfMonth() == 1 && fromDate.getYear() == toDate.getYear()) {
            // Năm
            if (fromDate.getMonthValue() == 1 && toDate.getDayOfMonth() == 31 && toDate.getMonthValue() == 12)
                return String.format("Năm %1$s", fromDate.getYear());
            // Quý
            // List<int> lstMonth = new int[] {1,4,7,10};
            if (Arrays.asList(1, 4, 7, 10).contains(fromDate.getMonthValue())) {
                List<String> chars = Arrays.asList("I", "II", "III", "IV");
                LocalDate test = fromDate.plusMonths(3).plusDays(-1).atTime(0, 0, 0, 0).toLocalDate();
                if (fromDate.plusMonths(3).plusDays(-1).atTime(0, 0, 0, 0).toLocalDate().equals(toDate.atTime(0, 0, 0, 0).toLocalDate()))
                    return String.format("Quý %1$s năm %2$s", chars.get(((fromDate.getMonthValue() - 1) / 3)), fromDate.getYear());
            }
            // tháng
            if (fromDate.plusMonths(1).plusDays(-1).atTime(0, 0, 0, 0).toLocalDate().equals(toDate.atTime(0, 0, 0, 0).toLocalDate()))
                return String.format("Tháng %1$s năm %2$s", fromDate.getMonthValue(), fromDate.getYear());
        }
        return String.format("Từ ngày %1$s đến ngày %2$s", convertDate(fromDate), convertDate(toDate));
    }

    public static String getValueString(String data) {
        if (data == null) {
            return "";
        } else {
            return data;
        }
    }

    public static void setValueCellDouble(Cell cell, BigDecimal data) {
        if (data == null || data.compareTo(BigDecimal.ZERO) == 0) {

        } else {
            cell.setCellValue(data.doubleValue());
        }
    }

    // Convert PostedDate, Date
    public static String convertDate(LocalDate date) {
        if (date == null) {
            return null;
        } else {
            return date.format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
        }
    }

    // Convert PostedDate, Date
    public static String convertDate_C_YYYYMMDD_HHMMSS(LocalDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.format(DateTimeFormatter.ofPattern(DateUtil.C_YYYYMMDD_HHMMSS));
        }
    }

    /**
     * @Author Hautv
     * @param lstSADT
     * @return
     */
    public static int checkQuantityDelivery(List<SAOrderDetails> lstSADT) {
        int result = 0;
        BigDecimal sumQuantityDelivery = BigDecimal.ZERO;
        for (SAOrderDetails saOrderDetails : lstSADT) {
            if (saOrderDetails.getQuantityDelivery() != null) {
                sumQuantityDelivery = sumQuantityDelivery.add(saOrderDetails.getQuantityDelivery());
                if (saOrderDetails.getQuantityDelivery().compareTo(saOrderDetails.getQuantity()) < 0) {
                    result = Constants.SAOrder.Status.DANG_THUC_HIEN;
                    break;
                } else if (saOrderDetails.getQuantityDelivery().compareTo(saOrderDetails.getQuantity()) >= 0) {
                    result = Constants.SAOrder.Status.DA_HOAN_THANH;
                }
            }
        }
        if (sumQuantityDelivery.compareTo(BigDecimal.ZERO) == 0) {
            return Constants.SAOrder.Status.CHUA_THUC_HIEN;
        }
        return result;
    }

    /** @Author Hautv
     * @param uuids
     * @return
     */
    public static List<UUID> convertListStringToListUUIDReverse(List<String> uuids) {
        List<UUID> result = new ArrayList<>();
        for (String id : uuids) {
            result.add(Utils.uuidConvertToGUID(UUID.fromString(id)));
        }
        return result;
    }

    /**
     * convert từ hàm bên js của Hậu
     * @param taxCode
     * @return
     */
    public static boolean checkTaxCode(String taxCode) {
        if (taxCode.length() <= 14 && taxCode.length() >= 10) {
            int value = getInt(taxCode, 0) * 31 +
                        getInt(taxCode, 1) * 29 +
                        getInt(taxCode, 2) * 23 +
                        getInt(taxCode, 3) * 19 +
                        getInt(taxCode, 4) * 17 +
                        getInt(taxCode, 5) * 13 +
                        getInt(taxCode, 6) * 7 +
                        getInt(taxCode, 7) * 5 +
                        getInt(taxCode, 8) * 3;
            int mod = 10 - value % 11;
            if (Math.abs(mod) == getInt(taxCode, 9)) {
                return true;
            }
        }
        return false;
    }

    public static int getInt(String s, int index) {
        return Character.codePointAt(s, index) - 48;
    }

}
