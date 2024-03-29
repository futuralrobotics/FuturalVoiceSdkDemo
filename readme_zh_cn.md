<a href="./readme.md">English</a>  <a href="./readme_zh_cn.md">中文</a>

### SDK使用说明
1. 导入aar文件及依赖的项目
   ![](./images/01.png)
2. 添加应用权限、App子类、网络配置及导入服务
   ![](./images/02.png)
---
### API使用说明
机器人整机启动成功后，app启动MonitorService时，MonitorService会与下位机通信，并发出FuturalConstants.ROS_READY广播，表示机器人整机处于ready状态
1. 电量信息 -- 机器人启动成功后，如下api可以获得当前电量百分比
```
   FuturalUtils.getPowerLevel()
```
2. 充电状态 -- 机器人启动成功后，如下api可以获得机器人是否在充电
```
   FuturalUtils.isCharging() true说明机器人正在充电
```
3. 急停状态 -- 机器人启动成功后，如下api可以获得急停状态
```
   FuturalUtils.getEmergence() true说明急停键被按下
```
4. 刹车状态 -- 机器人启动成功后，如下api可以获得刹车状态
```
   FuturalUtils.getBrake() true说明刹车键被按下
```
5. 定位质量 -- 机器人启动成功后，如下api可以获得定位质量，返回int类型数据，范围0-100，值越大说明定位越准确，当值小于50时，导航去某个点位时，出错的概率就越大
```
   FuturalUtils.getLocalizationQuality()
```
6. 获得当前地图的点位信息
```
   String jsonStr = FuturalUtils.getCurrentMapPointsInfo()
   List<PoisBean> positions = JSON.parseObject(jsonStr, new TypeReference<List<PoisBean>>() {});
```
7. 通过坐标导航去某个点位
```
   private ActionResultBean actionResultBean;
   private FuturalAPIUtils.ActionCallBack actionCallBack = new SlamAPIUtils.ActionCallBack() {
       @Override
       public void onResponse(ActionResultBean bean) {
           actionResultBean = bean;//保存越来，查询导航状态时使用
       }

       @Override
       public void onFailure() {
       }
   };

   PoisBean.PoseBean poseBean = new PoisBean.PoseBean();
   poseBean.setX(oriX);
   poseBean.setY(oriY);
   poseBean.setYaw(theta);
   FuturalAPIUtils.createMoveToAction(actionCallBack, poseBean);
```
8. 查询导航状态
```
   FuturalAPIUtils.checkAction((new SlamAPIUtils.ActionCallBack() {
       @Override
       public void onResponse(ActionResultBean bean) {
           Log.e(TAG, "checkAction, " + bean);
           if (isCanceling || handler.getActivity().isDestroyed()) return;

           //某些情况下，action id会被底盘重置，导致check ation时一直返回404，机器人就会停止在某点上，不动了，这种情况下重新导航去某点
           if (bean.getAction_id() == -404) {
               //请再调用一次导航去某个点的api
           } else if (bean != null && bean.getState().getStatus() == 4 && bean.getState().getResult() == 0) {
               Log.d(TAG, "arrived point");
           } else {
               //如果没有到达指定地点，请再一次检查
           }
       }
       @Override
       public void onFailure() {
       }
   }), actionResultBean.getAction_id());
```
9. 取消导航
```
   FuturalAPIUtils.cancelCurrentAction(new FuturalAPIUtils.ActionCallBack() {
       @Override
       public void onResponse(ActionResultBean bean) {
           Log.e(TAG, "cancelNav");
       }

       @Override
       public void onFailure() {
       }
   });
   简单的调用FuturalAPIUtils.cancelCurrentAction(null);
```