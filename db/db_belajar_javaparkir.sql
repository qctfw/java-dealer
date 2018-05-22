-- phpMyAdmin SQL Dump
-- version 4.8.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 22 Bulan Mei 2018 pada 22.36
-- Versi server: 10.1.31-MariaDB
-- Versi PHP: 7.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_belajar_javaparkir`
--

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `banding_kodetransaksi`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `banding_kodetransaksi` (
`maks_tborder` varchar(5)
,`maks_tbtransaksi` varchar(5)
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `tampil_order`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `tampil_order` (
`kd_order` varchar(5)
,`kd_transaksi` varchar(5)
,`kd_barang` varchar(5)
,`nama` varchar(33)
,`stok` int(6)
,`sub_total` bigint(10) unsigned
,`tgl_order` timestamp
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `tampil_stok`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `tampil_stok` (
`kd_barang` int(5)
,`nama` varchar(33)
,`jenis` enum('Mobil','Motor','Truk')
,`merk` varchar(20)
,`tahun` int(4)
,`stok` int(6)
,`harga` int(10) unsigned
);

-- --------------------------------------------------------

--
-- Stand-in struktur untuk tampilan `tampil_transaksi`
-- (Lihat di bawah untuk tampilan aktual)
--
CREATE TABLE `tampil_transaksi` (
`kd_transaksi` varchar(5)
,`pembeli` varchar(20)
,`kd_order` varchar(5)
,`nama` varchar(33)
,`merk` varchar(20)
,`stok` int(6)
,`sub_total` bigint(10) unsigned
,`total` bigint(10) unsigned
,`kembalian` bigint(10) unsigned
,`tgl_order` timestamp
,`tanggal_beli` timestamp
);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_merk`
--

CREATE TABLE `tb_merk` (
  `id_merk` int(3) NOT NULL,
  `merk` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_merk`
--

INSERT INTO `tb_merk` (`id_merk`, `merk`) VALUES
(2, 'Daihatsu'),
(13, 'Honda'),
(11, 'Hyundai'),
(3, 'Mazda'),
(14, 'Suzuki'),
(1, 'Toyota');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_order`
--

CREATE TABLE `tb_order` (
  `kd_order` varchar(5) NOT NULL,
  `kd_transaksi` varchar(5) NOT NULL,
  `kd_barang` varchar(5) NOT NULL,
  `stok` int(6) NOT NULL,
  `sub_total` bigint(10) UNSIGNED NOT NULL,
  `tgl_order` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_order`
--

INSERT INTO `tb_order` (`kd_order`, `kd_transaksi`, `kd_barang`, `stok`, `sub_total`, `tgl_order`) VALUES
('OR001', 'TR001', '1', 3, 12000000000, '2017-12-21 01:34:56'),
('OR002', 'TR002', '1', 3, 12000000000, '2018-02-08 02:11:09'),
('OR003', 'TR002', '1', 1, 4000000000, '2018-02-08 02:12:12'),
('OR004', 'TR002', '2', 2, 500000000, '2018-02-08 05:56:03'),
('OR005', 'TR003', '2', 1, 250000000, '2018-02-09 04:11:05'),
('OR006', 'TR003', '2', 2, 500000000, '2018-02-09 04:11:14'),
('OR007', 'TR004', '2', 2, 500000000, '2018-02-12 06:46:33'),
('OR008', 'TR005', '3', 6, 150000000, '2018-02-12 09:19:06'),
('OR009', 'TR006', '3', 2, 50000000, '2018-02-13 05:04:33'),
('OR010', 'TR006', '2', 2, 500000000, '2018-02-13 05:04:41'),
('OR011', 'TR006', '3', 1, 25000000, '2018-02-13 05:04:56'),
('OR012', 'TR007', '2', 2, 500000000, '2018-02-22 08:50:18'),
('OR013', 'TR008', '3', 2, 50000000, '2018-02-22 08:54:32'),
('OR014', 'TR009', '1', 1, 40000000, '2018-04-27 10:25:35');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_stok`
--

CREATE TABLE `tb_stok` (
  `kd_barang` int(5) NOT NULL,
  `nama` varchar(33) NOT NULL,
  `jenis` enum('Mobil','Motor','Truk') NOT NULL,
  `id_merk` int(3) NOT NULL,
  `tahun` year(4) NOT NULL,
  `stok` int(6) NOT NULL,
  `harga` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_stok`
--

INSERT INTO `tb_stok` (`kd_barang`, `nama`, `jenis`, `id_merk`, `tahun`, `stok`, `harga`) VALUES
(1, 'Daihatsu Xenia', 'Motor', 2, 2017, 19, 40000000),
(2, 'RX-8', 'Mobil', 1, 2008, 44, 250000000),
(3, 'Toyota Ferrari', 'Mobil', 1, 2010, 66, 25000000),
(4, 'Honda Beat', 'Motor', 13, 2015, 80, 8500000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `kd_transaksi` varchar(5) NOT NULL,
  `pembeli` varchar(20) NOT NULL,
  `total` bigint(10) UNSIGNED NOT NULL,
  `kembalian` bigint(10) UNSIGNED NOT NULL,
  `tanggal_beli` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`kd_transaksi`, `pembeli`, `total`, `kembalian`, `tanggal_beli`) VALUES
('TR001', 'Budi', 12000000000, 17500, '2017-12-21 01:35:50'),
('TR002', 'Abang', 16500000000, 100000, '2018-02-08 15:11:05'),
('TR003', 'Abang', 750000000, 1000100, '2018-02-12 06:36:40'),
('TR004', 'Abang', 500000000, 10000000, '2018-02-12 07:14:01'),
('TR005', 'User', 150000000, 10000000, '2018-02-12 09:19:32'),
('TR006', 'Abang', 575000000, 25000000, '2018-02-15 01:41:14'),
('TR007', 'Abang', 500000000, 10000000, '2018-02-22 08:50:34'),
('TR008', 'Abang', 50000000, 500000, '2018-02-22 08:54:47'),
('TR009', '400000000', 40000000, 360000000, '2018-04-27 10:26:24');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_user`
--

CREATE TABLE `tb_user` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `nama` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data untuk tabel `tb_user`
--

INSERT INTO `tb_user` (`username`, `password`, `nama`) VALUES
('admin', 'admin', 'Admin');

-- --------------------------------------------------------

--
-- Struktur untuk view `banding_kodetransaksi`
--
DROP TABLE IF EXISTS `banding_kodetransaksi`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `banding_kodetransaksi`  AS  select max(`tb_order`.`kd_transaksi`) AS `maks_tborder`,max(`tb_transaksi`.`kd_transaksi`) AS `maks_tbtransaksi` from (`tb_order` join `tb_transaksi`) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `tampil_order`
--
DROP TABLE IF EXISTS `tampil_order`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `tampil_order`  AS  select `tb_order`.`kd_order` AS `kd_order`,`tb_order`.`kd_transaksi` AS `kd_transaksi`,`tb_order`.`kd_barang` AS `kd_barang`,`tb_stok`.`nama` AS `nama`,`tb_order`.`stok` AS `stok`,`tb_order`.`sub_total` AS `sub_total`,`tb_order`.`tgl_order` AS `tgl_order` from (`tb_order` join `tb_stok` on((`tb_order`.`kd_barang` = `tb_stok`.`kd_barang`))) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `tampil_stok`
--
DROP TABLE IF EXISTS `tampil_stok`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `tampil_stok`  AS  select `tb_stok`.`kd_barang` AS `kd_barang`,`tb_stok`.`nama` AS `nama`,`tb_stok`.`jenis` AS `jenis`,`tb_merk`.`merk` AS `merk`,year(str_to_date(`tb_stok`.`tahun`,'%Y')) AS `tahun`,`tb_stok`.`stok` AS `stok`,`tb_stok`.`harga` AS `harga` from (`tb_stok` join `tb_merk` on((`tb_stok`.`id_merk` = `tb_merk`.`id_merk`))) ;

-- --------------------------------------------------------

--
-- Struktur untuk view `tampil_transaksi`
--
DROP TABLE IF EXISTS `tampil_transaksi`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `tampil_transaksi`  AS  select `tb_transaksi`.`kd_transaksi` AS `kd_transaksi`,`tb_transaksi`.`pembeli` AS `pembeli`,`tb_order`.`kd_order` AS `kd_order`,`tb_stok`.`nama` AS `nama`,`tb_merk`.`merk` AS `merk`,`tb_order`.`stok` AS `stok`,`tb_order`.`sub_total` AS `sub_total`,`tb_transaksi`.`total` AS `total`,`tb_transaksi`.`kembalian` AS `kembalian`,`tb_order`.`tgl_order` AS `tgl_order`,`tb_transaksi`.`tanggal_beli` AS `tanggal_beli` from (((`tb_transaksi` join `tb_order` on((`tb_transaksi`.`kd_transaksi` = `tb_order`.`kd_transaksi`))) join `tb_stok` on((`tb_stok`.`kd_barang` = `tb_order`.`kd_barang`))) join `tb_merk` on((`tb_stok`.`id_merk` = `tb_merk`.`id_merk`))) ;

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_merk`
--
ALTER TABLE `tb_merk`
  ADD PRIMARY KEY (`id_merk`),
  ADD UNIQUE KEY `merk` (`merk`);

--
-- Indeks untuk tabel `tb_order`
--
ALTER TABLE `tb_order`
  ADD PRIMARY KEY (`kd_order`);

--
-- Indeks untuk tabel `tb_stok`
--
ALTER TABLE `tb_stok`
  ADD PRIMARY KEY (`kd_barang`),
  ADD UNIQUE KEY `nama` (`nama`);

--
-- Indeks untuk tabel `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`kd_transaksi`);

--
-- Indeks untuk tabel `tb_user`
--
ALTER TABLE `tb_user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_merk`
--
ALTER TABLE `tb_merk`
  MODIFY `id_merk` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT untuk tabel `tb_stok`
--
ALTER TABLE `tb_stok`
  MODIFY `kd_barang` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
