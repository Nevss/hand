package com.darly.activities.common;

import java.util.ArrayList;

import android.graphics.Point;

import com.darly.activities.app.Constract;
import com.darly.activities.model.IARoomPoint;

/**
 * @author Administrator 现在已知情况下。房间号码和房间点阵。这是一个配置类。
 */
public class IAPoisDataConfig {

	/**
	 * @auther Darly Fronch 2015 下午3:19:29 TODO
	 * @params ks XML中的房间号码关系表，这些数据需要从服务器获取。包括下面的八百伴点阵表格。
	 */
	public static ArrayList<IARoomPoint> getModelTest(int[] ks, int key) {
		// TODO Auto-generated method stub

		ArrayList<IARoomPoint> arrayList = new ArrayList<IARoomPoint>();

		switch (key) {
		case 12:
			for (int i = 0; i < ks.length; i++) {
				ArrayList<Point> list = new ArrayList<Point>();
				for (int j = 0, sr = jingan[i].length; j < sr; j += 2) {
					list.add(new Point(jingan[i][j], jingan[i][j + 1]));
				}
				arrayList.add(new IARoomPoint(ks[i] + "", list));
			}
			break;
		case 24:
			for (int i = 0; i < ks.length; i++) {
				ArrayList<Point> list = new ArrayList<Point>();
				for (int j = 0, sr = xuhui[i].length; j < sr; j += 2) {
					list.add(new Point(xuhui[i][j], xuhui[i][j + 1]));
				}
				arrayList.add(new IARoomPoint(ks[i] + "", list));
			}
			break;
		case 31:
			for (int i = 0; i < ks.length; i++) {
				ArrayList<Point> list = new ArrayList<Point>();
				for (int j = 0, sr = babaiban[i].length; j < sr; j += 2) {
					list.add(new Point(babaiban[i][j], babaiban[i][j + 1]));
				}
				arrayList.add(new IARoomPoint(ks[i] + "", list));
			}
			break;
		default:
			break;
		}

		return arrayList;
	}

	// 获取平面图资料八百伴对应的平面图资料
	public static int babaibanw = 2000;
	public static int babaibanh = 1124;
	public static int[][] babaiban = {
			{/* 眼科 */Constract.width * 208 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 342 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 342 / babaibanw,
					Constract.width * 142 / babaibanw,
					Constract.width * 208 / babaibanw,
					Constract.width * 142 / babaibanw },
			{/* 眼科 */Constract.width * 346 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 142 / babaibanw,
					Constract.width * 346 / babaibanw,
					Constract.width * 142 / babaibanw },
			{/* 视力 */Constract.width * 488 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 622 / babaibanw,
					Constract.width * 38 / babaibanw,
					Constract.width * 622 / babaibanw,
					Constract.width * 142 / babaibanw,
					Constract.width * 488 / babaibanw,
					Constract.width * 142 / babaibanw },
			{ /* 备用 */Constract.width * 55 / babaibanw,
					Constract.width * 222 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 222 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 324 / babaibanw,
					Constract.width * 55 / babaibanw,
					Constract.width * 324 / babaibanw },
			{/* 外科 */Constract.width * 55 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 526 / babaibanw,
					Constract.width * 55 / babaibanw,
					Constract.width * 526 / babaibanw },
			{/* 外科 */Constract.width * 55 / babaibanw,
					Constract.width * 530 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 530 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 616 / babaibanw,
					Constract.width * 55 / babaibanw,
					Constract.width * 616 / babaibanw },
			{ /* 备用 */Constract.width * 55 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 748 / babaibanw,
					Constract.width * 55 / babaibanw,
					Constract.width * 748 / babaibanw },
			{/* 采血处 */Constract.width * 55 / babaibanw,
					Constract.width * 752 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 752 / babaibanw,
					Constract.width * 206 / babaibanw,
					Constract.width * 940 / babaibanw,
					Constract.width * 55 / babaibanw,
					Constract.width * 940 / babaibanw },
			{/* B超彩超室 */Constract.width * 260 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 368 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 368 / babaibanw,
					Constract.width * 327 / babaibanw,
					Constract.width * 260 / babaibanw,
					Constract.width * 327 / babaibanw },
			{/* B超彩超室 */Constract.width * 372 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 327 / babaibanw,
					Constract.width * 372 / babaibanw,
					Constract.width * 327 / babaibanw },
			{/* B超 彩超室 */Constract.width * 488 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 327 / babaibanw,
					Constract.width * 488 / babaibanw,
					Constract.width * 327 / babaibanw },
			{/* 内科 */Constract.width * 260 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 368 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 368 / babaibanw,
					Constract.width * 436 / babaibanw,
					Constract.width * 260 / babaibanw,
					Constract.width * 436 / babaibanw },
			{/* 内科 */Constract.width * 372 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 484 / babaibanw,
					Constract.width * 436 / babaibanw,
					Constract.width * 372 / babaibanw,
					Constract.width * 436 / babaibanw },
			{/* 心电图 */Constract.width * 488 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 620 / babaibanw,
					Constract.width * 436 / babaibanw,
					Constract.width * 488 / babaibanw,
					Constract.width * 436 / babaibanw },
			{/* 内科 */Constract.width * 426 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 426 / babaibanw,
					Constract.width * 540 / babaibanw },
			{/* 内科 */Constract.width * 428 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 438 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 428 / babaibanw,
					Constract.width * 540 / babaibanw },
			{/* 心电图 */Constract.width * 258 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 370 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 370 / babaibanw,
					Constract.width * 648 / babaibanw,
					Constract.width * 258 / babaibanw,
					Constract.width * 648 / babaibanw },
			{/* 心电图 */Constract.width * 374 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 648 / babaibanw,
					Constract.width * 374 / babaibanw,
					Constract.width * 648 / babaibanw },
			{/* 心电图110 */Constract.width * 374 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 544 / babaibanw,
					Constract.width * 540 / babaibanw,
					Constract.width * 648 / babaibanw,
					Constract.width * 374 / babaibanw,
					Constract.width * 648 / babaibanw },
			{/* 耳鼻喉 */Constract.width * 258 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 330 / babaibanw,
					Constract.width * 810 / babaibanw,
					Constract.width * 258 / babaibanw,
					Constract.width * 810 / babaibanw },
			{/* 耳鼻喉 */Constract.width * 334 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 406 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 406 / babaibanw,
					Constract.width * 810 / babaibanw,
					Constract.width * 334 / babaibanw,
					Constract.width * 810 / babaibanw },
			{/* 一般检查 */Constract.width * 410 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 498 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 498 / babaibanw,
					Constract.width * 810 / babaibanw,
					Constract.width * 410 / babaibanw,
					Constract.width * 810 / babaibanw },
			{/* 外科 */Constract.width * 502 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 590 / babaibanw,
					Constract.width * 652 / babaibanw,
					Constract.width * 590 / babaibanw,
					Constract.width * 810 / babaibanw,
					Constract.width * 502 / babaibanw,
					Constract.width * 810 / babaibanw },
			{/* B超彩超室 */Constract.width * 674 / babaibanw,
					Constract.width * 188 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 188 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 313 / babaibanw,
					Constract.width * 674 / babaibanw,
					Constract.width * 313 / babaibanw },
			{/* 妇科 */Constract.width * 674 / babaibanw,
					Constract.width * 388 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 388 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 517 / babaibanw,
					Constract.width * 674 / babaibanw,
					Constract.width * 517 / babaibanw },
			{/* 妇科 */Constract.width * 674 / babaibanw,
					Constract.width * 520 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 520 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 648 / babaibanw,
					Constract.width * 674 / babaibanw,
					Constract.width * 648 / babaibanw },
			{/* 内科 */Constract.width * 674 / babaibanw,
					Constract.width * 654 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 654 / babaibanw,
					Constract.width * 820 / babaibanw,
					Constract.width * 780 / babaibanw,
					Constract.width * 674 / babaibanw,
					Constract.width * 780 / babaibanw },
			{/* 口腔科 */Constract.width * 1204 / babaibanw,
					Constract.width * 182 / babaibanw,
					Constract.width * 1395 / babaibanw,
					Constract.width * 182 / babaibanw,
					Constract.width * 1298 / babaibanw,
					Constract.width * 277 / babaibanw },
			{/* 中医科 */Constract.width * 1300 / babaibanw,
					Constract.width * 280 / babaibanw,
					Constract.width * 1420 / babaibanw,
					Constract.width * 162 / babaibanw,
					Constract.width * 1478 / babaibanw,
					Constract.width * 220 / babaibanw,
					Constract.width * 1356 / babaibanw,
					Constract.width * 338 / babaibanw },
			{/* 钼靶室 */Constract.width * 1361 / babaibanw,
					Constract.width * 340 / babaibanw,
					Constract.width * 1481 / babaibanw,
					Constract.width * 224 / babaibanw,
					Constract.width * 1541 / babaibanw,
					Constract.width * 282 / babaibanw,
					Constract.width * 1420 / babaibanw,
					Constract.width * 400 / babaibanw },
			{/* DR */Constract.width * 1528 / babaibanw,
					Constract.width * 424 / babaibanw,
					Constract.width * 1609 / babaibanw,
					Constract.width * 345 / babaibanw,
					Constract.width * 1645 / babaibanw,
					Constract.width * 345 / babaibanw,
					Constract.width * 1645 / babaibanw,
					Constract.width * 462 / babaibanw,
					Constract.width * 1537 / babaibanw,
					Constract.width * 462 / babaibanw,
					Constract.width * 1537 / babaibanw,
					Constract.width * 424 / babaibanw },
			{/* CT室 */Constract.width * 1648 / babaibanw,
					Constract.width * 345 / babaibanw,
					Constract.width * 1808 / babaibanw,
					Constract.width * 345 / babaibanw,
					Constract.width * 1808 / babaibanw,
					Constract.width * 462 / babaibanw,
					Constract.width * 1648 / babaibanw,
					Constract.width * 462 / babaibanw },
			{/* 动脉硬化 */Constract.width * 1717 / babaibanw,
					Constract.width * 552 / babaibanw,
					Constract.width * 1808 / babaibanw,
					Constract.width * 467 / babaibanw,
					Constract.width * 1808 / babaibanw,
					Constract.width * 474 / babaibanw,
					Constract.width * 1866 / babaibanw,
					Constract.width * 529 / babaibanw,
					Constract.width * 1777 / babaibanw,
					Constract.width * 613 / babaibanw },
			{/* 备用 */Constract.width * 1811 / babaibanw,
					Constract.width * 639 / babaibanw,
					Constract.width * 1893 / babaibanw,
					Constract.width * 556 / babaibanw,
					Constract.width * 1950 / babaibanw,
					Constract.width * 611 / babaibanw,
					Constract.width * 1867 / babaibanw,
					Constract.width * 694 / babaibanw },
			{/* 骨密度 */Constract.width * 1678 / babaibanw,
					Constract.width * 772 / babaibanw,
					Constract.width * 1808 / babaibanw,
					Constract.width * 642 / babaibanw,
					Constract.width * 1864 / babaibanw,
					Constract.width * 698 / babaibanw,
					Constract.width * 1735 / babaibanw,
					Constract.width * 829 / babaibanw },
			{/* 经颅多普勒 */Constract.width * 1542 / babaibanw,
					Constract.width * 907 / babaibanw,
					Constract.width * 1675 / babaibanw,
					Constract.width * 775 / babaibanw,
					Constract.width * 1731 / babaibanw,
					Constract.width * 832 / babaibanw,
					Constract.width * 1600 / babaibanw,
					Constract.width * 965 / babaibanw },
			{/* 肺功能 */Constract.width * 1425 / babaibanw,
					Constract.width * 940 / babaibanw,
					Constract.width * 1570 / babaibanw,
					Constract.width * 940 / babaibanw,
					Constract.width * 1600 / babaibanw,
					Constract.width * 970 / babaibanw,
					Constract.width * 1600 / babaibanw,
					Constract.width * 1042 / babaibanw,
					Constract.width * 1425 / babaibanw,
					Constract.width * 1042 / babaibanw },
			{/* 000 */Constract.width * 823 / babaibanw,
					Constract.width * 196 / babaibanw,
					Constract.width * 942 / babaibanw,
					Constract.width * 311 / babaibanw,
					Constract.width * 878 / babaibanw,
					Constract.width * 376 / babaibanw,
					Constract.width * 823 / babaibanw,
					Constract.width * 321 / babaibanw } };

	// 获取平面图资料静安对应的平面图资料

	public static int jinganw = 2000;
	public static int jinganh = 1124;
	public static int[][] jingan = {
			{/* 检验科 */Constract.width * 518 / jinganw,
					Constract.width * 151 / jinganw,
					Constract.width * 780 / jinganw, Constract.width * 9 / jinganw,
					Constract.width * 802 / jinganw, Constract.width * 9 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 110 / jinganw,
					Constract.width * 518 / jinganw,
					Constract.width * 269 / jinganw },
			{/* DR房間 */Constract.width * 806 / jinganw,
					Constract.width * 9 / jinganw,
					Constract.width * 1079 / jinganw,
					Constract.width * 9 / jinganw,
					Constract.width * 1079 / jinganw,
					Constract.width * 112 / jinganw,
					Constract.width * 806 / jinganw,
					Constract.width * 112 / jinganw },
			{/* 钼钯 */Constract.width * 1082 / jinganw,
					Constract.width * 9 / jinganw,
					Constract.width * 1247 / jinganw,
					Constract.width * 9 / jinganw,
					Constract.width * 1247 / jinganw,
					Constract.width * 112 / jinganw,
					Constract.width * 1082 / jinganw,
					Constract.width * 112 / jinganw },
			// 进行修改
			{/* 备用227 */Constract.width * 1236 / jinganw,
					Constract.width * 170 / jinganw,
					Constract.width * 1329 / jinganw,
					Constract.width * 87 / jinganw,
					Constract.width * 1351 / jinganw,
					Constract.width * 110 / jinganw,
					Constract.width * 1260 / jinganw,
					Constract.width * 194 / jinganw },
			{/* 肺功能228 */Constract.width * 1259 / jinganw,
					Constract.width * 198 / jinganw,
					Constract.width * 1354 / jinganw,
					Constract.width * 115 / jinganw,
					Constract.width * 1408 / jinganw,
					Constract.width * 166 / jinganw,
					Constract.width * 1317 / jinganw,
					Constract.width * 251 / jinganw },
			{/* 骨密度 */Constract.width * 1065 / jinganw,
					Constract.width * 144 / jinganw,
					Constract.width * 1148 / jinganw,
					Constract.width * 228 / jinganw,
					Constract.width * 1092 / jinganw,
					Constract.width * 285 / jinganw,
					Constract.width * 1008 / jinganw,
					Constract.width * 201 / jinganw },
			{/* 经颅多普勒 */Constract.width * 1003 / jinganw,
					Constract.width * 204 / jinganw,
					Constract.width * 1124 / jinganw,
					Constract.width * 324 / jinganw,
					Constract.width * 1069 / jinganw,
					Constract.width * 378 / jinganw,
					Constract.width * 948 / jinganw,
					Constract.width * 260 / jinganw },
			{/* B超219 */Constract.width * 452 / jinganw,
					Constract.width * 313 / jinganw,
					Constract.width * 583 / jinganw,
					Constract.width * 313 / jinganw,
					Constract.width * 583 / jinganw,
					Constract.width * 392 / jinganw,
					Constract.width * 452 / jinganw,
					Constract.width * 392 / jinganw },
			{/* B超218 */Constract.width * 218 / jinganw,
					Constract.width * 313 / jinganw,
					Constract.width * 446 / jinganw,
					Constract.width * 313 / jinganw,
					Constract.width * 446 / jinganw,
					Constract.width * 392 / jinganw,
					Constract.width * 218 / jinganw,
					Constract.width * 392 / jinganw },
			{/* 心电图217 */Constract.width * 218 / jinganw,
					Constract.width * 395 / jinganw,
					Constract.width * 422 / jinganw,
					Constract.width * 395 / jinganw,
					Constract.width * 422 / jinganw,
					Constract.width * 465 / jinganw,
					Constract.width * 218 / jinganw,
					Constract.width * 465 / jinganw },
			{/* 内科238 */Constract.width * 1128 / jinganw,
					Constract.width * 331 / jinganw,
					Constract.width * 1201 / jinganw,
					Constract.width * 402 / jinganw,
					Constract.width * 1146 / jinganw,
					Constract.width * 456 / jinganw,
					Constract.width * 1072 / jinganw,
					Constract.width * 384 / jinganw },
			{/* 239 */Constract.width * 1186 / jinganw,
					Constract.width * 268 / jinganw,
					Constract.width * 1260 / jinganw,
					Constract.width * 340 / jinganw,
					Constract.width * 1203 / jinganw,
					Constract.width * 398 / jinganw,
					Constract.width * 1130 / jinganw,
					Constract.width * 325 / jinganw },
			{/* 226 */Constract.width * 1318 / jinganw,
					Constract.width * 258 / jinganw,
					Constract.width * 1413 / jinganw,
					Constract.width * 174 / jinganw,
					Constract.width * 1470 / jinganw,
					Constract.width * 226 / jinganw,
					Constract.width * 1374 / jinganw,
					Constract.width * 311 / jinganw },
			{/* 报告室 */Constract.width * 1473 / jinganw,
					Constract.width * 230 / jinganw,
					Constract.width * 1617 / jinganw,
					Constract.width * 374 / jinganw,
					Constract.width * 1314 / jinganw,
					Constract.width * 374 / jinganw },
			{/* B超253 */Constract.width * 1623 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1725 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1725 / jinganw,
					Constract.width * 508 / jinganw,
					Constract.width * 1623 / jinganw,
					Constract.width * 508 / jinganw },
			{/* 男外科 252 */Constract.width * 1527 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1619 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1619 / jinganw,
					Constract.width * 508 / jinganw,
					Constract.width * 1527 / jinganw,
					Constract.width * 508 / jinganw },
			{/* 男外科 251 */Constract.width * 1425 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1523 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1523 / jinganw,
					Constract.width * 508 / jinganw,
					Constract.width * 1425 / jinganw,
					Constract.width * 508 / jinganw },
			{/* 心电图 250 */Constract.width * 1305 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1415 / jinganw,
					Constract.width * 377 / jinganw,
					Constract.width * 1415 / jinganw,
					Constract.width * 508 / jinganw,
					Constract.width * 1305 / jinganw,
					Constract.width * 508 / jinganw },
			{/* 心电图 216 */Constract.width * 221 / jinganw,
					Constract.width * 469 / jinganw,
					Constract.width * 424 / jinganw,
					Constract.width * 469 / jinganw,
					Constract.width * 424 / jinganw,
					Constract.width * 539 / jinganw,
					Constract.width * 221 / jinganw,
					Constract.width * 539 / jinganw },
			{/* 妇科212 */Constract.width * 220 / jinganw,
					Constract.width * 544 / jinganw,
					Constract.width * 348 / jinganw,
					Constract.width * 544 / jinganw,
					Constract.width * 348 / jinganw,
					Constract.width * 711 / jinganw,
					Constract.width * 220 / jinganw,
					Constract.width * 711 / jinganw },
			{/* 妇科213 */Constract.width * 352 / jinganw,
					Constract.width * 543 / jinganw,
					Constract.width * 470 / jinganw,
					Constract.width * 543 / jinganw,
					Constract.width * 470 / jinganw,
					Constract.width * 623 / jinganw,
					Constract.width * 352 / jinganw,
					Constract.width * 623 / jinganw },
			{/* 外科215 */Constract.width * 475 / jinganw,
					Constract.width * 543 / jinganw,
					Constract.width * 585 / jinganw,
					Constract.width * 543 / jinganw,
					Constract.width * 585 / jinganw,
					Constract.width * 623 / jinganw,
					Constract.width * 475 / jinganw,
					Constract.width * 623 / jinganw },
			{/* B超255 */Constract.width * 1649 / jinganw,
					Constract.width * 513 / jinganw,
					Constract.width * 1726 / jinganw,
					Constract.width * 513 / jinganw,
					Constract.width * 1726 / jinganw,
					Constract.width * 593 / jinganw,
					Constract.width * 1649 / jinganw,
					Constract.width * 593 / jinganw },
			{/* 备用237 */Constract.width * 1293 / jinganw,
					Constract.width * 625 / jinganw,
					Constract.width * 1368 / jinganw,
					Constract.width * 559 / jinganw,
					Constract.width * 1408 / jinganw,
					Constract.width * 596 / jinganw,
					Constract.width * 1334 / jinganw,
					Constract.width * 666 / jinganw },
			{/* 备用236 */Constract.width * 1147 / jinganw,
					Constract.width * 769 / jinganw,
					Constract.width * 1220 / jinganw,
					Constract.width * 696 / jinganw,
					Constract.width * 1261 / jinganw,
					Constract.width * 737 / jinganw,
					Constract.width * 1184 / jinganw,
					Constract.width * 814 / jinganw },
			{/* 眼科208 */Constract.width * 665 / jinganw,
					Constract.width * 615 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 615 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 699 / jinganw,
					Constract.width * 665 / jinganw,
					Constract.width * 699 / jinganw },
			{/* B超211 */Constract.width * 220 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 373 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 373 / jinganw,
					Constract.width * 796 / jinganw,
					Constract.width * 220 / jinganw,
					Constract.width * 796 / jinganw },
			{/* B超210 */Constract.width * 379 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 479 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 479 / jinganw,
					Constract.width * 796 / jinganw,
					Constract.width * 379 / jinganw,
					Constract.width * 796 / jinganw },
			{/* 内科209 */Constract.width * 482 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 584 / jinganw,
					Constract.width * 716 / jinganw,
					Constract.width * 584 / jinganw,
					Constract.width * 796 / jinganw,
					Constract.width * 482 / jinganw,
					Constract.width * 796 / jinganw },
			{/* 视力207 */Constract.width * 665 / jinganw,
					Constract.width * 704 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 704 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 791 / jinganw,
					Constract.width * 665 / jinganw,
					Constract.width * 791 / jinganw },
			{/* 233 */Constract.width * 806 / jinganw,
					Constract.width * 612 / jinganw,
					Constract.width * 839 / jinganw,
					Constract.width * 612 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 719 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 804 / jinganw,
					Constract.width * 806 / jinganw,
					Constract.width * 804 / jinganw },
			{/* 备用235 */Constract.width * 898 / jinganw,
					Constract.width * 549 / jinganw,
					Constract.width * 1011 / jinganw,
					Constract.width * 663 / jinganw,
					Constract.width * 954 / jinganw,
					Constract.width * 720 / jinganw,
					Constract.width * 840 / jinganw,
					Constract.width * 608 / jinganw },
			{/* 231 */Constract.width * 1038 / jinganw,
					Constract.width * 818 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 818 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 876 / jinganw,
					Constract.width * 1038 / jinganw,
					Constract.width * 876 / jinganw },
			{/* 232 */Constract.width * 806 / jinganw,
					Constract.width * 808 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 808 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 885 / jinganw,
					Constract.width * 806 / jinganw,
					Constract.width * 885 / jinganw },
			{/* 耳鼻喉科206 */Constract.width * 663 / jinganw,
					Constract.width * 796 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 796 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 905 / jinganw,
					Constract.width * 663 / jinganw,
					Constract.width * 905 / jinganw },
			{/* 备用205 */Constract.width * 662 / jinganw,
					Constract.width * 908 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 908 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 961 / jinganw,
					Constract.width * 662 / jinganw,
					Constract.width * 961 / jinganw },
			{/* 备用203 */Constract.width * 662 / jinganw,
					Constract.width * 965 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 965 / jinganw,
					Constract.width * 802 / jinganw,
					Constract.width * 1022 / jinganw,
					Constract.width * 662 / jinganw,
					Constract.width * 1022 / jinganw },
			{/* 一般检查228 */Constract.width * 806 / jinganw,
					Constract.width * 889 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 889 / jinganw,
					Constract.width * 946 / jinganw,
					Constract.width * 1021 / jinganw,
					Constract.width * 806 / jinganw,
					Constract.width * 1021 / jinganw },
			{/* 230 */Constract.width * 1037 / jinganw,
					Constract.width * 880 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 880 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 931 / jinganw,
					Constract.width * 1037 / jinganw,
					Constract.width * 931 / jinganw },
			{/* 229 */Constract.width * 1037 / jinganw,
					Constract.width * 936 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 936 / jinganw,
					Constract.width * 1177 / jinganw,
					Constract.width * 1032 / jinganw,
					Constract.width * 1037 / jinganw,
					Constract.width * 1032 / jinganw },
			{/* 采血处201 */Constract.width * 501 / jinganw,
					Constract.width * 984 / jinganw,
					Constract.width * 501 / jinganw,
					Constract.width * 1111 / jinganw,
					Constract.width * 627 / jinganw,
					Constract.width * 1111 / jinganw } };

	// 获取平面图资料TC2对应的平面图资料()徐汇地区的平面图
	public static int xuhuiw = 2000;
	public static int xuhuih = 1124;
	public static int[][] xuhui = {
			{/* DR房間136 */Constract.width * 231 / jinganw,
					Constract.width * 24 / jinganw,
					Constract.width * 428 / jinganw,
					Constract.width * 24 / jinganw,
					Constract.width * 428 / jinganw,
					Constract.width * 172 / jinganw,
					Constract.width * 231 / jinganw,
					Constract.width * 172 / jinganw },
			{/* CT房間 137 */Constract.width * 431 / jinganw,
					Constract.width * 24 / jinganw,
					Constract.width * 766 / jinganw,
					Constract.width * 24 / jinganw,
					Constract.width * 766 / jinganw,
					Constract.width * 172 / jinganw,
					Constract.width * 431 / jinganw,
					Constract.width * 172 / jinganw },
			{/* 采血处104 */Constract.width * 1590 / jinganw,
					Constract.width * 595 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 595 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 767 / jinganw,
					Constract.width * 1590 / jinganw,
					Constract.width * 767 / jinganw },
			{/* 检验科105 */Constract.width * 1590 / jinganw,
					Constract.width * 770 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 770 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 945 / jinganw,
					Constract.width * 1590 / jinganw,
					Constract.width * 945 / jinganw },
			{/* 一般项目106 */Constract.width * 1536 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1732 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 1536 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* 口腔科107 */Constract.width * 1416 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1533 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1533 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 1416 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* 心电图108 */Constract.width * 1294 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1412 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1412 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 1294 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* 心电图109 */Constract.width * 1164 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1291 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1291 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 1164 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* B超110 */Constract.width * 1062 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1160 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1160 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 1062 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* B超111 */Constract.width * 962 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1059 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 1059 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 962 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* B超112 */Constract.width * 833 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 958 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 958 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 833 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* 钼靶124 */Constract.width * 563 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 829 / jinganw,
					Constract.width * 991 / jinganw,
					Constract.width * 829 / jinganw,
					Constract.width * 1102 / jinganw,
					Constract.width * 563 / jinganw,
					Constract.width * 1102 / jinganw },
			{/* 多普勒129 */Constract.width * 338 / jinganw,
					Constract.width * 572 / jinganw,
					Constract.width * 562 / jinganw,
					Constract.width * 572 / jinganw,
					Constract.width * 562 / jinganw,
					Constract.width * 711 / jinganw,
					Constract.width * 338 / jinganw,
					Constract.width * 711 / jinganw },
			{/* 呼气试验肺功能130 */Constract.width * 228 / jinganw,
					Constract.width * 572 / jinganw,
					Constract.width * 335 / jinganw,
					Constract.width * 572 / jinganw,
					Constract.width * 335 / jinganw,
					Constract.width * 711 / jinganw,
					Constract.width * 228 / jinganw,
					Constract.width * 711 / jinganw },
			{/* 中医科131 */Constract.width * 228 / jinganw,
					Constract.width * 440 / jinganw,
					Constract.width * 340 / jinganw,
					Constract.width * 440 / jinganw,
					Constract.width * 340 / jinganw,
					Constract.width * 536 / jinganw,
					Constract.width * 228 / jinganw,
					Constract.width * 536 / jinganw },
			{/* 备用室132 */Constract.width * 228 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 340 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 340 / jinganw,
					Constract.width * 408 / jinganw,
					Constract.width * 228 / jinganw,
					Constract.width * 408 / jinganw },
			{/* 动脉粥样硬化133 */Constract.width * 342 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 452 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 452 / jinganw,
					Constract.width * 408 / jinganw,
					Constract.width * 342 / jinganw,
					Constract.width * 408 / jinganw },
			{/* 人体成分骨密度134 */Constract.width * 455 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 566 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 566 / jinganw,
					Constract.width * 408 / jinganw,
					Constract.width * 455 / jinganw,
					Constract.width * 408 / jinganw },
			{/* 视力116 */Constract.width * 1279 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 678 / jinganw,
					Constract.width * 1279 / jinganw,
					Constract.width * 678 / jinganw },
			{/* 眼科115 */Constract.width * 1279 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 761 / jinganw,
					Constract.width * 1279 / jinganw,
					Constract.width * 761 / jinganw },
			{/* 眼科114 */Constract.width * 1279 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 840 / jinganw,
					Constract.width * 1279 / jinganw,
					Constract.width * 840 / jinganw },
			{/* 耳鼻喉113 */Constract.width * 1279 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 1435 / jinganw,
					Constract.width * 924 / jinganw,
					Constract.width * 1279 / jinganw,
					Constract.width * 924 / jinganw },
			{/* 男外科120 */Constract.width * 1118 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 678 / jinganw,
					Constract.width * 1118 / jinganw,
					Constract.width * 678 / jinganw },
			{/* 中医科119 */Constract.width * 1118 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 761 / jinganw,
					Constract.width * 1118 / jinganw,
					Constract.width * 761 / jinganw },
			{/* 男内科118 */Constract.width * 1118 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 840 / jinganw,
					Constract.width * 1118 / jinganw,
					Constract.width * 840 / jinganw },
			{/* 男外科117 */Constract.width * 1118 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 1274 / jinganw,
					Constract.width * 924 / jinganw,
					Constract.width * 1118 / jinganw,
					Constract.width * 924 / jinganw },
			{/* 女心电图123 */Constract.width * 891 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 697 / jinganw,
					Constract.width * 891 / jinganw,
					Constract.width * 697 / jinganw },
			{/* B超122 */Constract.width * 891 / jinganw,
					Constract.width * 702 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 702 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 801 / jinganw,
					Constract.width * 891 / jinganw,
					Constract.width * 801 / jinganw },
			{/* B超121 */Constract.width * 891 / jinganw,
					Constract.width * 805 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 805 / jinganw,
					Constract.width * 1024 / jinganw,
					Constract.width * 925 / jinganw,
					Constract.width * 891 / jinganw,
					Constract.width * 925 / jinganw },
			{/* 女内科128 */Constract.width * 632 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 598 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 678 / jinganw,
					Constract.width * 632 / jinganw,
					Constract.width * 678 / jinganw },
			{/* 女外科127 */Constract.width * 632 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 682 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 761 / jinganw,
					Constract.width * 632 / jinganw,
					Constract.width * 761 / jinganw },
			{/* 妇科126 */Constract.width * 632 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 765 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 840 / jinganw,
					Constract.width * 632 / jinganw,
					Constract.width * 840 / jinganw },
			{/* 妇科125 */Constract.width * 632 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 844 / jinganw,
					Constract.width * 788 / jinganw,
					Constract.width * 924 / jinganw,
					Constract.width * 632 / jinganw,
					Constract.width * 924 / jinganw },
			{/* 妇科000 */Constract.width * 1523 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 1731 / jinganw,
					Constract.width * 256 / jinganw,
					Constract.width * 1731 / jinganw,
					Constract.width * 356 / jinganw,
					Constract.width * 1523 / jinganw,
					Constract.width * 356 / jinganw } };

}
