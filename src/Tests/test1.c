int main() {
 int x = 2;
float y = 3.3;
 switch (x) {
 case 1: {
 y = 10; // 简单语句
 break;
}
 case 2: {
 y = 20; // 简单赋值语句
 func(y); // 函数调⽤
 break;
}
 default: { // 处理未匹配的情况
 return 0; // return 语句
}
 }
}