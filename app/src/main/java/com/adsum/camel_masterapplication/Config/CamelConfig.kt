package com.adsum.camel_masterapplication.Config

object CamelConfig {

    var WEBURL = "https://uaqcrc.com/wp-json/camel/v1/"


    var getSchedulImage = "get_race_scheduling"
    var racelistArchiveMonthYear="racelistArchiveMonthYear"
    var Year_list="archiveMonthYear"
    val get_term_condition_details="get_term_and_condition_detail"
    val getcamelcategory = "getcategory"
    var get_notification = "get_notification"
    val logout="logout"
    val viewuser="viewuser"

    val LogOut="logout?user_id="
    val userList="viewuser"
    val login="auth?username={username}&password={password}"
    val racelist= "get_race_detail"
    val subracelist="race_view_detail?raceid="
    val removeRaceList="rmvracedetail?race_id"
    val updateRaceSchedule="race_date"
    val profileupdate="updateProfileDetails"
    val profile="getProfileDetails?user_id="
    val profileimage="updateProfilePicture"
    val get_sub_category_list="viewRoundlist"
    val malelist="viewCamel?user_id="
    var removeCamel="delCamel?id="
    var selectedUserList="add_participant_in_race?race_id="
    var addUser = "adduser"
    val participateinrace = "participate_in_Race"
    val ViewRoundMemberlisting = "viewRoundMemberlisting"



   // var racelistArchive="racelistArchiveMonthYear?year={year}&month={month}&user_id={user_id}"







      var remove="removeRoundMemberOption?rl_id="
    var APP_VERSION: String? = null






    val addcamel = "addCamel?camel={camel}&gender={gender}&status={status}&user_id={user_id}"
    val addCamelMember = "addRoundMemberOption?race_id={race_id}&round_id={round_id}&user_id={user_id}&camel_id={camel_id}"
   // var Year_list="racelistArchiveMonthYear?year={year}&month={month}&user_id={user_id}"



}