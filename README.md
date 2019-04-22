# hkr_system-engineering 个人逼逼和功能描述
- 前端设计由林倩郁完成，后台数据对接由我通过一些demo学着完成，垃圾桶列表和信息界面数据由公司提供的api获取，涉及到保密协议什么的接口信息删掉了_(:з」∠)
- 其实都是些最基本的简单功能，登录界面用的还是不安全的jdbc，直接明文传数据，下次再学用php。但是不得不说到现在还是很有成就感
- message界面原先设想的是有推送功能，还没学会，之后试着改改

- log in  && log out && forget password
  - Store the user's related data on the server, and connect to the database through jdbc to verify the user's identity. When the user name and password are consistent, the user can enter the personal interface.
  - Save user login data through SharedPreferences to avoid repeated login operations, and remove related data when users need to log out.
  - forget password
    - The user can enter the email address corresponding to his/her own account. The system will automatically send the verification code using javamail and smtp. When the verification code is verified, the database will update the user's password.
- view trash bin list
  - Get data related to the trash through the API provided by the company, and display it with different color icons according to its level.
  - The data is automatically updated daily, and if needed, the user can press the refresh button to get the latest data.
- modify personal info
  - modify password
    - Users can verify their identity by entering the old password and enter a new password to modify it.
    - After the user identity is verified, connect to the database through jdbc and modify the user password in the database.
  - modify email
    - The system will automatically send the verification code using javamail and smtp. When the verification code is verified, the database will update the user's email as the forgotten password.
- view message
  - The latest garbage bin data is filtered to correspond to the area that the current user is responsible for. If the trash can is too full, the system will send relevant information to the user so that the user can grasp the trash information that needs to be cleaned up at present.
    In other words, the user only needs to go to the message interface to know the trash can that needs to be cleaned up.
- 

