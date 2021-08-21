# -*- coding: utf-8 -*-
# This is a sample Python script.

# Press ⌃R to execute it or replace it with your code.
# Press Double ⇧ to search everywhere for classes, files, tool windows, actions, and settings.
import getopt
import os
import re
import sys

from PIL import Image
from PIL import ImageDraw

COLOR_BLACK = (0, 0, 0, 255)
PNG = '.png'

"""
目前支持解析的类型
"""
TYPE_BG = 'bg'


# .9图属性定义
class NinePatchConfig(object):
    # .9图可拉伸位置，以宽高比定义，范围（0,1）
    xr = yr = 0.0
    # 内容区，以上下左右padding的像素值表示
    lp = tp = rp = bp = 0

    def __init__(self, xr, yr, lp, tp, rp, bp):
        self.xr = xr
        self.yr = yr
        self.lp = lp
        self.tp = tp
        self.rp = rp
        self.bp = bp


# 背景图属性
class BgNinePatchConfig(NinePatchConfig):
    def __init__(self):
        super(BgNinePatchConfig, self).__init__(0.5, 0.5, 24, 12, 72, 12)


# 粉丝团属性，待扩展
class FansGroupNinePatchConfig(NinePatchConfig):
    def __init__(self):
        super(FansGroupNinePatchConfig, self).__init__(0.5, 0.5, 24, 12, 72, 10)


def is_png_file(file_name):
    """
    判断是否是png文件，非png文件不支持
    """
    if not isinstance(file_name, str):
        return False
    if file_name.lower().endswith(PNG):
        return True
    return False


def is_valid_bg_file(bg_file_path: str, pkg) -> bool:
    file: Image.Image = Image.open(bg_file_path)
    if file:
        return file.width == 243 and file.height == 87
    return False


def encode_bg_and_save(source_file_path):
    """
    通过原背景图片，生成一张Android apk识别的.9图，并在当前目录生成两个文件夹
    1、temp:存放绘制了黑线的.9图，做校验调试使用
    2、encode：存放编译后的.9图，素材中台使用此图片
    :param source_file_path: 原文件路径
    """
    p = BgNinePatchConfig
    encode_and_save(source_file_path, p)


def draw_ninepatch_path(source, patch_x, patch_y, y_start, y_end, x_start, x_end):
    """
    将原图转换绘制上.9相关的数据，可拉伸点，内容区，坐标均以原图为基准
    :param source: 原图
    :param patch_x: 拉伸点x坐标
    :param patch_y: 拉伸点y坐标
    :param y_start: 垂直方向内容区开始位置
    :param y_end: 垂直方向内容区结束位置
    :param x_start: 水平方向内容区开始位置
    :param x_end: 水平方向内容区结束位置
    :return:图片
    """
    assert isinstance(source, Image.Image)
    width = source.width
    height = source.height
    # 创建一个透明画布，先将原图绘制上去
    image = Image.new(mode='RGBA', size=(width + 2, height + 2), color=(0, 0, 0, 0))
    canvas = ImageDraw.Draw(image)

    image.paste(im=source, box=(1, 1, width + 1, height + 1), mask=None)
    # 拉伸点
    canvas.rectangle(xy=(patch_x + 1, 0, patch_x + 1, 0), fill=COLOR_BLACK, outline=None, width=0)
    canvas.rectangle(xy=(0, patch_y + 1, 0, patch_y + 1), fill=COLOR_BLACK, outline=None, width=0)
    # 内容区
    canvas.rectangle(xy=(width + 1, y_start + 1, width + 1, y_end), fill=COLOR_BLACK, outline=None, width=0)
    canvas.rectangle(xy=(x_start + 1, height + 1, x_end, height + 1), fill=COLOR_BLACK, outline=None, width=0)
    return image


def appt_encode(source_file_path, target_file_path):
    """
    将.9图进行编译
    :param source_file_path: 原图路径
    :param target_file_path: 编译后的文件路径
    """
    os.system(f"aapt s -i {source_file_path} -o {target_file_path}")


def encode_and_save_list(files_dir, config):
    """
    批量编译.9图片
    :param config:
    :param files_dir: 文件夹路径
    """
    files = os.listdir(files_dir)
    png_files = list(filter(is_png_file, files))
    print(png_files)
    for index in range(len(png_files)):
        encode_and_save(files_dir + '/' + png_files[index], config)


def encode_and_save(source_file_path, config):
    """
       通过原背景图片，生成一张Android apk识别的.9图，并在当前目录生成两个文件夹
       1、temp:存放绘制了黑线的.9图，做校验调试使用
       2、encode：存放编译后的.9图，素材中台使用此图片
       :param config: .9图属性
       :param source_file_path: 原文件路径
       """
    source = Image.open(source_file_path)
    assert isinstance(source_file_path, str)

    strings = source_file_path.split(sep='/')
    image_name = strings[len(strings) - 1]
    pkg_path = source_file_path[:-len(image_name)]
    # 不带后缀的文件名，用于生成目标图片使用
    image_name_without_suffix = None
    if image_name.lower().endswith(PNG):
        image_name_without_suffix = image_name[:-len(PNG)]
    # 仅支持png图片
    if image_name_without_suffix is None:
        return

    assert isinstance(source, Image.Image)
    width = source.width
    height = source.height
    draw = draw_ninepatch_path(source, patch_x=width * config.xr, patch_y=height * config.yr,
                               y_start=config.tp, y_end=height - config.bp, x_start=config.lp, x_end=width - config.rp)
    temp_dir = pkg_path + 'temp'
    if not os.path.exists(temp_dir):
        os.makedirs(temp_dir)
    temp_path = f"{temp_dir}/{image_name_without_suffix}.9{PNG}"
    draw.save(temp_path)
    encode_dir = pkg_path + "encode"
    if not os.path.exists(encode_dir):
        os.makedirs(encode_dir)
    appt_encode(temp_path, f"{encode_dir}/{image_name_without_suffix}.9{PNG}")


def main(argv):
    source_type = ''
    file = ''
    pkg = ''
    try:
        opts, args = getopt.getopt(argv, '-h-t:-f:-p:', ['type=', 'file=', 'package='])
    except getopt.GetoptError:
        print('error')
        sys.exit(2)
    for opt, arg in opts:
        if opt in ('-h', '--help'):
            print('usage编译单个文件: np -t <type> -f <file_path>')
            print('usage编译一组文件: np -t <type> -p <package_dir>')
            print(f'usage支持的type类型: {TYPE_BG}<背景图处理>')
            sys.exit(0)
        elif opt in ('-t', '--type'):
            source_type = arg
        elif opt in ('-f', '--file'):
            file = arg
        elif opt in ('-p', '--package'):
            pkg = arg
    config = None
    if source_type == TYPE_BG:
        config = BgNinePatchConfig()
    if config is None:
        print(f"error unknown type {source_type}")
        sys.exit(2)

    if len(file) > 0:
        encode_and_save(file, config)
    if len(pkg) > 0:
        encode_and_save_list(pkg, config)


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    main(sys.argv[1:])
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
