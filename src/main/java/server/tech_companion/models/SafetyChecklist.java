package server.tech_companion.models;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "safetyChecklists")
public class SafetyChecklist {
    @Id
    private ObjectId _id;
    private String serviceAddress;
    private String typeOfGate;
    private String locationOfGate;
    private String operatorModel;
    private boolean isMasterSlave;
    private boolean hasSafetyLoops;
    private boolean hasExitLoops;
    private String loopDetails;
    private List<String> photoEyes;
    private boolean hasPhotoEye;
    private boolean hasSafetyPost;
    private boolean hasDoorGuard;
    private boolean hasMesh;
    private boolean hasCloseSafetyEdge;
    private boolean hasOpenSafetyEdge;
    private boolean hasRubberBumper;
    private boolean hasEntrapment;
    private boolean hasMagLock;

    // overheads
    private boolean isBalanced;
    private boolean hasCorrectHardware;
    private String hardwareModel;
    
    // swing gates
    private boolean hasCenterLoop;
    private boolean hasArmPinchPoint;
}