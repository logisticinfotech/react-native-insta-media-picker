import {
  requireNativeComponent,
  NativeEventEmitter,
  View,
  PermissionsAndroid,
} from "react-native";
import React from "react";

const InstaPicker = requireNativeComponent("InstaPicker");
const eventEmitter = new NativeEventEmitter(InstaPicker);

class NativeInstaPicker extends React.Component {
  constructor(props) {
    super(props);
  }

  componentDidMount = () => {
    eventEmitter.addListener("onNextPress", this.onNextPress);
    eventEmitter.addListener("onBackPress", this.onBackPress);
  };

  onBackPress = (event) => {
    {
      Platform.OS === "ios"
        ? console.log("RN back press  =====>", event.nativeEvent.message)
        : console.log("RN back press  =====> ");
      this.setState({ start: false });
    }
  };
  onNextPress = (event) => {
    {
      Platform.OS === "ios"
        ? console.log("this is event=-=-=-=>", event.nativeEvent.url)
        : console.log("this is event=-=-=-=>", event.filePath);
      this.setState({ start: false });
    }
  };

  render() {
    return (
      <View>
        {Platform.OS === "android" ? (
          <InstaPicker style={{ height: "100%", width: "100%" }} />
        ) : (
          <View style={{ flex: 1, width: "100%", height: "100%" }}>
            <InstaPicker
              onNextPress={this.onNextPress}
              onBackPress={this.onBackPress}
            />
          </View>
        )}
      </View>
    );
  }
}

export default NativeInstaPicker;
