package com.southwind;

import com.southwind.service.CartService;
import com.southwind.service.ProductCategoryService;
import com.southwind.service.ProductService;
import com.southwind.service.UserAddressService;
import com.southwind.utils.DescriptionParser;
import com.southwind.vo.ProductCategoryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@SpringBootTest
class MmallApplicationTests {

    @Autowired
    private ProductCategoryService service;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private ProductService productService;

    @Test
    void contextLoads() {
        List<ProductCategoryVO> productCategoryVOList = this.service.buildProductCategoryMenu();
        int i = 0;
    }


    @Test
    void parse(){
        String description = "主板芯片\n集成芯片 声卡/网卡\n主芯片组 Intel Z790\n芯片组描述 采用Intel Z790芯片组\n音频芯片 集成Realtek 7.1声道音效芯片\n网卡芯片 板载2.5千兆网卡\n处理器规格\nCPU类型 第十四代/第十三代/第十二代 Core/Pentium/Celeron\nCPU插槽 LGA 1700\n内存规格\n内存类型 4×DDR5 DIMM\n最大内存容量 128GB\n内存描述 支持双通道DDR5 7600(OC) /7400(OC) /7200(OC) /7000(OC) /6800(OC) /6600(OC) / 6400(OC) / 6200(OC) / 6000(OC) / 5800(OC) / 5600(OC) / 5400(OC) / 5200(OC) / 4800 / 4000MHz内存\n存储扩展\nPCI-E标准 PCI-E 5.0，PCI-E 4.0，PCI-E 3.0\nPCI-E X16插槽 1个\nPCI-E X4插槽 1个\nPCI-E X1插槽 3个\n存储接口 4×M.2接口，6×SATA III接口\nI/O接口\nUSB（背板） 1×USB3.2 Gen2x2 Type-C接口\n1×USB3.2 Gen2接口\n4×USB3.2 Gen1接口\n4×USB2.0接口\nUSB（内置） 1×USB3.2 Gen1 Type-C\n1×USB3.2 Gen1\n2×USB2.0\n视频接口 1×HDMI接口，1×DisplayPort接口\n电源接口 两个8针，一个24针电源接口\n其它接口 1×SMA天线接口，1×RJ45网络接口，3×音频接口";
        Map<String, String> details = DescriptionParser.parseDescription(description);
        String chipset = details.get("内存类型");
        String brand = DescriptionParser.extractBrandFromChipset(chipset);
        System.out.println("Extracted brand from chipset: " + brand);
        System.out.println(chipset);

    }
    @Test
    void SSDParse(){
        String description = "容量描述：单条（16GB） 内存类型：DDR4 内存主频：3200MHz工作电压：1.2V";
        String details = DescriptionParser.parseSSDDescription(description);
        System.out.println(details);
    }
    @Test
    void SSDParse1(){
        String memory = "2×DDR5 DIMM";
        int i = memory.indexOf("DDR4");
        System.out.println(i);
    }
    @Test
    void testyingPan(){
        String description = "接口类型： M.2PCIe接口";
        String board = "主板芯片\n" +
                "集成芯片\t声卡/网卡\n" +
                "主芯片组\tIntel Z790\n" +
                "芯片组描述\t采用Intel Z790芯片组\n" +
                "音频芯片\t集成Realtek 7.1声道音效芯片\n" +
                "网卡芯片\t板载2.5千兆网卡\n" +
                "处理器规格\n" +
                "CPU类型\t第十四代/第十三代/第十二代 Core/Pentium/Celeron\n" +
                "CPU插槽\tLGA 1700\n" +
                "内存规格\n" +
                "内存类型\t4×DDR5 DIMM\n" +
                "最大内存容量\t128GB\n" +
                "内存描述\t支持双通道DDR5 7600(OC) /7400(OC) /7200(OC) /7000(OC) /6800(OC) /6600(OC) / 6400(OC) / 6200(OC) / 6000(OC) / 5800(OC) / 5600(OC) / 5400(OC) / 5200(OC) / 4800 / 4000MHz内存\n" +
                "存储扩展\n" +
                "PCI-E标准\tPCI-E 5.0，PCI-E 4.0，PCI-E 3.0\n" +
                "PCI-E X16插槽\t1个\n" +
                "PCI-E X4插槽\t1个\n" +
                "PCI-E X1插槽\t3个\n" +
                "存储接口\t4×M.2接口，6×SATA III接口\n" +
                "I/O接口\n" +
                "USB（背板）\t1×USB3.2 Gen2x2 Type-C接口\n" +
                "1×USB3.2 Gen2接口\n" +
                "4×USB3.2 Gen1接口\n" +
                "4×USB2.0接口\n" +
                "USB（内置）\t1×USB3.2 Gen1 Type-C\n" +
                "1×USB3.2 Gen1\n" +
                "2×USB2.0\n" +
                "视频接口\t1×HDMI接口，1×DisplayPort接口\n" +
                "电源接口\t两个8针，一个24针电源接口\n" +
                "其它接口\t1×SMA天线接口，1×RJ45网络接口，3×音频接口";
        String details = DescriptionParser.extractYingPanInterfaceType(description);
        Map<String, String> detail = DescriptionParser.parseDescription(board);
        String yingpan = detail.get("存储接口");
        System.out.println(details);
        System.out.println(yingpan);
        System.out.println(DescriptionParser.compareStrings(details, yingpan));

    }




}
