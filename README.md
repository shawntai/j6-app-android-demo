# Background
J6 is a facility management app developed by Siemens. Its main functionality currently include displaying work orders, creating new work orders, editing existing ones, and taking attendance.

# HomePageActivity.kt
The landing page of the app. At the bottom of the screen is an expansive layout where all work orders are shown. The layout contains various tabs, each loading a corresponding fragment. For example, on tapping “Work Order”, the `WorkOrderFragment` is loaded as so:
`loadFragment(WorkOrderFragment())`

# WorkOrderFragment.kt
This contains an array of work order appointments stored in the database. Initially, it loads the list of work orders from the server API. It can be called by making use of the `WorkOrderService` singleton object (explained in detail later). Here, to fetch the list of work orders, WorkOrderFragment first needs to implement the `FetchWorkOrdersCallback` interface, set the `fetchWorkOrdersListener` within `WorkOrderService` to itself, call `WorkOrderService.fetchWorkOrdersData()`, then implement `onWorkOrdersResult()` by overriding the method.
```
class WorkOrderFragment: Fragment(), FetchWorkOrdersCallback {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		WorkOrderService.fetchWorkOrdersListener = this
        	WorkOrderService.fetchWorkOrdersData()
	}
	override fun onWorkOrdersResult(result: List<WorkOrder>) {
		// do things with the result
	}
}
```
Apart from loading the work orders from the API, it is also allowed to add new ones. By clicking the ad button on the right side of the work order bar, `WorkOrderActivity.kt` is launched to allow the addition of new work orders. Once a new work order is created in `WorkOrderActivity` and the activity is finished, the new work order object created will be passed back and added to the database. 
```
class WorkOrderFragment: Fragment(), CreateWorkOrderCallBack {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		WorkOrderService.createWorkOrderCallBack = this
	}
	fun onNewWorkOrderAdded(newWorkOrder: WorkOrder) {
		// called when a newly created work order is passed in
        WorkOrderService.createWorkOrder(newWorkOrder)
    }
	override fun onWorkOrderCreated(workOrder: WorkOrder) {
        // work order added to database, fetch work orders data again
        WorkOrderService.fetchWorkOrdersData()
    }
}
```
# WorkOrderActivity.kt
This activity allows users to either add a new work order or edit an existing one after loading it from the database. It first attempts to load an existing work order as so:`workOrderImported = Gson().fromJson(intent.getStringExtra("work_order"), WorkOrder::class.java)`. If `workOrderImported` is not null, it loads the work order into the layout by filling in the corresponding views. If `workOrderImported` is null, that means the user is opting to create a new work order. There are a number of fields to fill in in order to create a work order in several ways. 
- Fill in an EditText
- Pop-up, scrollable selection menu (library used: `WheelPicker`), e.g.
```
val wheelPicker = findViewById<WheelPicker>(R.id.status_wp)
wheelPicker.data = listOf("Complete", "Cancel", "Outstanding order", "Request for outsource")
wheelPicker.setOnItemSelectedListener { picker, data, position ->
    findViewById<TextView>(R.id.status).text = data.toString() // fill the selected value into the corresponding view
}
```
For some selection menus, the options are loaded from the API instead of hardcoded. For example, for the field “category”, the available options categoryList are fetched via `WorkOrderService` as below:
 
```
class WorkOrderActivity : CategoriesCallback {
	private val categoryList = ArrayList<String>()
	override fun onCreate(savedInstanceState: Bundle?) {
        WorkOrderService.categoriesListener = this
        WorkOrderService.fetchCategoriesData()
	}
    override fun onCategoriesResult(result: List<String>) {
        categoryList.addAll(result)
        findViewById<WheelPicker>(R.id.category_wp).data = categoryList
    }
}
```

- Date/time selection

This pops up a window for date and time selection. Worth noting that there are several time-related fields, including requested time, estimated time, issued time, etc., so there are four time selection modes represented by four constants, namely

    - private val SELECT_START_TIME = 0
    - private val SELECT_FINISH_TIME = 1
    - private val SELECT_ISSUED_TIME = 2
    - private val SELECT_REQUEST_TIME = 3

- Autofill 

There are some fields that are filled automatically. For example, for the “Contact Information” category, the name of the tenant is an AutoCompleteTextView, where the available options are fetched via the API. Once a tenant is selected, the “Unit” and “Contact Person” fields, which are associated with the tenant, will automatically be filled in.
```
val tenant_actv = findViewById<AutoCompleteTextView>(R.id.tenant)
tenant_actv.setAdapter(ArrayAdapter(this, android.R.layout.select_dialog_item, tenantNameList))
tenant_actv.doOnTextChanged { text, start, before, count ->
    for (tenant in result) {
        if (tenant.name == text.toString()) {
            tenantSelected = tenant
            findViewById<EditText>(R.id.unit).setText(tenant.unit)
            findViewById<EditText>(R.id.contact_person).setText(tenant.contactPerson.name)
        }
    }
}
```

- Open up a new activity for further selection
    - `AddNewLocationActivity`: add a new location from the database
    - `SelectLocationActivity`: shows the list of location added and allows the user to delete locations by swiping left
    - `SelectEmployeeActivity`: add employees responsible for this work order
    - `AddNewMaterialActivity`: add new materials from the database and specify number of units
    - `SelectLocationActivity`: shows the list of materials added and allows the user to delete locations by swiping left
    - `AddEquipmentActivity`: add new equipment from the database by specifying the equipment id
    - `SelectEquipmentActivity`: shows the list of equipments added and allows the user to delete locations by swiping left

Once all the fields are filled in, users can click “Done” on the top-right corner to create/update a work order.

# WorkOrderService.kt
It is a singleton and uses the Retrofit library for API calls. Each category has its own callback, a listener within WorkOrderService and a function call. This contains a number of API calls available, as enlisted below.
- Employee: `fetchEmployeesData()`, callback returns `List<Employee>`
- Material: `fetchMaterialsData()`, callback returns `List<Material>`
- Location: `fetchLocationsData()`, callback returns `List<Location>`
- …
- …..
- …….
- Fetching existing work orders: `fetchWorkOrdersData()`, callback returns `List<WorkOrder>`
- Creating new work orders: `createWorkOrder(newWorkOrder)`, callback returns `WorkOrder`
…
# Attendance
When the user’s profile picture is clicked in `HomePageActivity`, it launches `AttendanceDialog` to allow the user to take attendance. The dialog contains a map and employee/time/location information.
 
The MapxusMap can be initialized like this:
```
mapView = findViewById(R.id.mapView)
mapView.onCreate(savedInstanceState)
mapviewProvider = MapboxMapViewProvider(context, mapView)
mapviewProvider!!.getMapxusMapAsync(this)
```
And the exact location of the user can be fetched with `MapxusPositioningProvider`. Need to implement `OnMapxusMapReadyCallback` and set the `locationProvider` in the overridden `onMapxusMapReady` method. It also needs to implement `OnLocationProvidedCallback` in order to check the eligibility to take attendance when the current location is fetched.
```
class AttendanceDialog(context: Context) : Dialog(context), OnMapxusMapReadyCallback, OnLocationProvidedCallback {
	...	
	override fun onMapxusMapReady(p0: MapxusMap?) {
        this.mapxusMap = p0
        mapxusPositioningProvider = activity?.let {
            MapxusPositioningProvider(
                it,
                context.applicationContext
            )
        }
        mapxusPositioningProvider!!.setListener(this)
        p0!!.setLocationProvider(mapxusPositioningProvider)
    }     
    override fun onLocationProvided(location: IndoorLocation) {
        // current location gotten, check if in AMC building
        canTakeAttendance = location.building == "143859d5c0fd4d76ba5c650f707bdfe7" // AMC
    }
	...
}
```
Lastly, the dialog can be called from like this:
```
val dialog = AttendanceDialog(this)
dialog.activity = this
dialog.show()
```
# To-do list
- Add multi-langauge support
- The function to edit work orders is not working and should be fixed. The changes are not being updated to the database. Might need to take a look at how the PATCH request is written in `WorkOrderService.kt`

# Deployment Guide
### Generate Signed APK
1. First, in Android Studio, go to the Build tab and select Generate Signed Bundle / APK...

![image](https://i.imgur.com/0DtCWGv.png)

2. For "key store path", choose "CREATE NEW..." if there is not any existing ones.

![image](https://i.imgur.com/FsshZTM.png)

3. A "New Key Store" window will pop up. Fill in the required information, then click OK. Remember to memorize the key store password, key alias, and key password, as they will be neede later on.

![image](https://i.imgur.com/6SHsP5N.png)

4. In the "Generate Signed Bundle or APK window, fill in the key store password, key alias, and key password of the key store you just created, and lick NEXT.

![image](https://i.imgur.com/Q2tjJPW.png)

5. For "Choose Variants", select the "release" build. Then select both V1 (Jar Signature) and V2 (Full APK signature) for the Signature Versions. Then click FINISH.

![image](https://i.imgur.com/D0kUglC.png)

6. An APK named "app-release.apk" will be generated and saved in the app/release directory.

![image](https://i.imgur.com/wJx8mUh.png)

### Deploy to Google Play Console
7. On Google Play Console, select "Internal testing" from the navigation bar on the left side, and select "Create new release" on the top-right corner.

![image](https://i.imgur.com/Tx69ZKY.png)

8. Under "App bundles and APKs" click "Upload" to upload to APK. Worth noting that before uploading the apk, you should make sure the version code is not the same as any previous version; otherwise, the console will not allow the upload until the version code is incremented. The version code can be changed in the app’s build.gradle, in
```
android {
    ...
     defaultConfig {
        ...
        versionCode 6 // increment
        versionName "1.0.5" // change to next version name
        ...
    }
}
```

![image](https://i.imgur.com/xXQwUov.png)

If the version code is not updated, this error will occur

![image](https://i.imgur.com/1UoFvKM.png)

9. After the successful upload of apk, fill in the release details. Then click Save, Review Release, and Save again to release the internal testing to testers.

![image](https://i.imgur.com/0k3wfM2.png)




